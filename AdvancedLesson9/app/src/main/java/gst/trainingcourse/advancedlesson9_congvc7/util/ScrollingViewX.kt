package gst.trainingcourse.advancedlesson9_congvc7.util

import androidx.core.view.ScrollingView

val ScrollingView.verticalScrollProgress: Float
    get() {
        val offset = computeVerticalScrollOffset()
        val extent = computeVerticalScrollExtent()
        val range = computeVerticalScrollRange()

        return offset / (range - extent).toFloat()
    }
