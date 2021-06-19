package gst.trainingcourse.advancedlesson78_congvc7.ui.base

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface LifecycleAwareFlowCollector {
    fun <T> Flow<T>.collectWhenStarted(action: suspend (T) -> Unit): Job
}
