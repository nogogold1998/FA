package gst.trainingcourse.lesson7_8_ex1_congvc7.util

import android.view.LayoutInflater
import android.view.View

val View.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)
