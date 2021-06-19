package gst.trainingcourse.advancedlesson78_congvc7.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import gst.trainingcourse.advancedlesson78_congvc7.R
import gst.trainingcourse.advancedlesson78_congvc7.ui.base.LifecycleAwareFlowCollector
import gst.trainingcourse.advancedlesson78_congvc7.util.hideSoftKeyboard
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity(), LifecycleAwareFlowCollector {
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel.hideSoftKeyboardRequest.collectWhenStarted {
            hideSoftKeyboard()
        }
    }

    override fun <T> Flow<T>.collectWhenStarted(action: suspend (T) -> Unit): Job {
        return lifecycleScope.launchWhenStarted { collect(action) }
    }
}
