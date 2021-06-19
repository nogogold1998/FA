package gst.trainingcourse.lesson1417_congvc7.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import gst.trainingcourse.lesson1417_congvc7.MyApplication
import gst.trainingcourse.lesson1417_congvc7.data.source.remote.OpenWeatherApiService
import gst.trainingcourse.lesson1417_congvc7.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var factory: MainVM.Factory

    private val mainVM: MainVM by viewModels { factory }

    private val adapter by lazy { WeatherEntryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        setupBinding()
        setContentView(binding.root)

        val api = OpenWeatherApiService.create()
        api.fetchCurrentWeather("Hanoi").subscribe { c ->
            Log.d(MainActivity::class.simpleName, "onCreate: $c")
        }
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this
        binding.mainVM = this.mainVM
        binding.recyclerViewAdapter = adapter
    }
}
