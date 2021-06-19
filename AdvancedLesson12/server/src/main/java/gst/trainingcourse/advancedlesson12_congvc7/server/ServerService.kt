package gst.trainingcourse.advancedlesson12_congvc7.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import gst.trainingcourse.advancedlesson12_congvc7.contract.IRemoteTrainee
import gst.trainingcourse.advancedlesson12_congvc7.contract.IRemoteTraineeCallback
import gst.trainingcourse.advancedlesson12_congvc7.contract.Trainee
import gst.trainingcourse.advancedlesson12_congvc7.server.data.AppDatabase
import gst.trainingcourse.advancedlesson12_congvc7.server.data.TraineeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val TAG = "TAG"

class ServerService : Service() {
    private val traineeDao: TraineeDao by lazy {
        AppDatabase.getInstance(this).traineeDao
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val binder by lazy {
        object : IRemoteTrainee.Stub() {
            override fun add(trainee: Array<out Trainee>): LongArray {
                Log.d(TAG, "add: ${Thread.currentThread().name}")
                return runBlocking(scope.coroutineContext) {
                    traineeDao.add(*trainee)
                }
            }

            override fun update(trainee: Array<out Trainee>): Int {
                return runBlocking(scope.coroutineContext) {
                    traineeDao.update(*trainee)
                }
            }

            override fun findName(name: String): Trainee? {
                return runBlocking(scope.coroutineContext) {
                    traineeDao.findName(name)
                }
            }

            private val callbackJobs = mutableMapOf<IRemoteTraineeCallback, Job>()
            private val bestTraineeFlow = traineeDao.queryBestTrainee()
                .stateIn(
                    scope = scope,
                    started = SharingStarted.Lazily,
                    initialValue = null
                )

            override fun registerBestTrainee(callback: IRemoteTraineeCallback): Boolean =
                synchronized(callbackJobs) {
                    if (callback in callbackJobs.keys) return false
                    callbackJobs[callback] = scope.launch {
                        bestTraineeFlow.collect { callback.onChanged(it) }
                    }
                    return true
                }

            override fun unregisterBestTrainee(callback: IRemoteTraineeCallback): Boolean =
                synchronized(callbackJobs) {
                    if (callback !in callbackJobs) return false
                    callbackJobs.remove(callback)!!.cancel()
                    return true
                }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: ${Thread.currentThread().name}")
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
