package gst.trainingcourse.lesson10_congvc7.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

fun <T1, T2, R> combine(
    init: R,
    scope: CoroutineScope,
    o1: Observable<T1>,
    o2: Observable<T2>,
    transform: suspend (T1, T2) -> R,
): Observable<R> {
    var cache1: T1? = null
    var cache2: T2? = null
    val lock = Semaphore(1, 0)
    val result = MutableObservable(init)

    o1.addObserver { t1 ->
        cache1 = t1
        cache2?.let { t2 ->
            scope.launch {
                lock.withPermit {
                    result.emit(transform(t1, t2))
                }
            }
        }
    }

    o2.addObserver { t2 ->
        cache2 = t2
        cache1?.let { t1 ->
            scope.launch {
                lock.withPermit {
                    result.emit(transform(t1, t2))
                }
            }
        }
    }

    return object : Observable<R> by result {}
}

inline fun <T, R> Observable<T>.transform(
    init: R,
    scope: CoroutineScope,
    crossinline transform: suspend CollectableObservableScope<R>.(T) -> Unit,
): Observable<R> {
    val result = MutableObservable(init)
    val collectableScope = object : CollectableObservableScope<R> {
        override fun setValue(value: R) {
            result.value = value
        }

        override suspend fun emit(value: R) {
            result.emit(value)
        }
    }
    this.addObserver {
        scope.launch {
            collectableScope.transform(it)
        }
    }
    return result
}

interface CollectableObservableScope<in T> {
    fun setValue(value: T)

    suspend fun emit(value: T)
}
