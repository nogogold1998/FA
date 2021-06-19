package gst.trainingcourse.lesson10_congvc7.core

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.yield

fun interface Observer<in T> {
    fun onNotify(value: T)
}

interface Observable<T> {
    val value: T

    val observers: Set<Observer<T>>

    /** @return if [observer] is either added or already added to this [MutableObservable] */
    @AnyThread
    fun addObserver(observer: Observer<T>): Boolean

    /** @return if [observer] is observing this [MutableObservable] and successfully be removed */
    @AnyThread
    fun removeObserver(observer: Observer<T>): Boolean
}

interface MutableObservable<T> : Observable<T> {
    override var value: T

    suspend fun emit(value: T): Job

    @AnyThread
    fun releaseObservers()
}

@Suppress("FunctionName")
fun <T> MutableObservable(init: T): MutableObservable<T> = DefaultMutableObservable(init)

private class DefaultMutableObservable<T>(init: T) : MutableObservable<T> {
    private val lock = Semaphore(1, 0)

    private var _value: T = init

    override var value: T
        set(value) {
            if (_value == value) return
            _value = value
            notifyAllObservers()
        }
        get() = _value

    private val _observers = mutableSetOf<Observer<T>>()
    override val observers: Set<Observer<T>> get() = _observers

    @WorkerThread
    override suspend fun emit(value: T) = coroutineScope {
        lock.acquire()
        _value = value
        launch {
            try {
                observers.forEach { observer ->
                    yield()
                    observer.onNotify(value)
                }
            } finally {
                lock.release()
            }
        }
    }

    /** no thread-safe guarantee */
    private fun notifyAllObservers(value: T = _value) {
        observers.forEach { observer ->
            observer.onNotify(value)
        }
    }

    override fun addObserver(observer: Observer<T>): Boolean =
        _observers.add(observer).also { observer.onNotify(value) }

    override fun removeObserver(observer: Observer<T>): Boolean = _observers.remove(observer)

    override fun releaseObservers() {
        _observers.clear()
    }
}
