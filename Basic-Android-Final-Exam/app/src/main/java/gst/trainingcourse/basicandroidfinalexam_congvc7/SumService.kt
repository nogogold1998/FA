package gst.trainingcourse.basicandroidfinalexam_congvc7

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.os.bundleOf
import gst.trainingcourse.basicandroidfinalexam_congvc7.SumServiceContract.ACTION_SUM
import gst.trainingcourse.basicandroidfinalexam_congvc7.SumServiceContract.EXTRA_SUM_FROM
import gst.trainingcourse.basicandroidfinalexam_congvc7.SumServiceContract.EXTRA_SUM_TO

object SumServiceContract {
    private const val REF = "gst.trainingcourse.basicandroidfinalexam_congvc7.SumService"

    const val ACTION_SUM = "$REF.ACTION_SUM"
    const val EXTRA_SUM_FROM = "$REF.EXTRA_SUM_FROM"
    const val EXTRA_SUM_TO = "$REF.EXTRA_SUM_TO"
}

private const val DELAY_INTERVAL = 5_000L

class SumService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.getSumActionParameters()?.let { (from, to) ->
            // startForeground()
            Thread {
                val sum = calculateSum(from, to)
                Thread.sleep(DELAY_INTERVAL)
                broadcastResult(sum)
            }.start()
        }
        return START_NOT_STICKY
    }

    private fun broadcastResult(sum: Long) {
        val resultIntent = MainActivity.provideDisplaySumResultIntent(sum)
         // LocalBroadcastManager.getInstance(this)
         //    .sendBroadcast(resultIntent)
        sendBroadcast(resultIntent)
    }

    private fun calculateSum(from: Int, to: Int): Long =
        (from..to).fold(0L) { accumulate: Long, number: Int ->
            accumulate + number
        }

    private fun Intent.getSumActionParameters(): Pair<Int, Int>? {
        if (action != ACTION_SUM || !hasExtra(EXTRA_SUM_FROM) || !hasExtra(EXTRA_SUM_TO)) return null
        return getIntExtra(EXTRA_SUM_FROM, -1) to getIntExtra(EXTRA_SUM_TO, -1)
    }

    companion object {
        fun provideSumIntent(context: Context, from: Int, to: Int) =
            Intent(context, SumService::class.java).apply {
                action = ACTION_SUM
                val extras = bundleOf(
                    EXTRA_SUM_FROM to from,
                    EXTRA_SUM_TO to to,
                )
                putExtras(extras)
            }
    }
}
