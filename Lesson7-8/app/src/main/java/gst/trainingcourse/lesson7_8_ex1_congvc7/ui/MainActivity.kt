package gst.trainingcourse.lesson7_8_ex1_congvc7.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import gst.trainingcourse.lesson7_8_ex1_congvc7.R
import kotlinx.coroutines.flow.first

class MainActivity : AppCompatActivity() {
    private val mainVM: MainActivityVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launchWhenStarted {
            mainVM.listAudioRequest.first().let{
                findViewById<View>(R.id.bottomMediaPlaybackBarContainer).visibility = View.VISIBLE
            }

        }
    }
}
