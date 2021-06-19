package gst.trainingcourse.lesson10_ex1_congvc7

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gst.trainingcourse.lesson10_ex1_congvc7.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() = with(binding) {
        check(::binding.isInitialized)

        buttonStartCommunicate.setOnClickListener {
            val startCommunicationIntent = ClientService.getIntent(this@MainActivity).apply {
                action = ClientService.ACTION_START_COMMUNICATION
            }
            startService(startCommunicationIntent)
        }
    }
}
