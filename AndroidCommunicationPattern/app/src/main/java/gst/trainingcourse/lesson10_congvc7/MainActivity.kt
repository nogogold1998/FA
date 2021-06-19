package gst.trainingcourse.lesson10_congvc7

import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import gst.trainingcourse.lesson10_congvc7.core.MutableObservable
import gst.trainingcourse.lesson10_congvc7.core.Observable
import gst.trainingcourse.lesson10_congvc7.databinding.ActivityMainBinding
import java.lang.Integer.max

interface MainActivityContract {
    val counter: Observable<Int>
    val isPlay: Observable<Boolean>
}

class MainActivity : AppCompatActivity(), MainActivityContract {
    private val _counter = MutableObservable(0)
    override val counter: Observable<Int> get() = _counter

    private lateinit var binding: ActivityMainBinding

    private val _isPlay = MutableObservable(false)
    override val isPlay: Observable<Boolean> get() = _isPlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        observeObservables()
    }

    private fun setupViews() = with(binding) {
        numberEditText.addTextChangedListener {
            if (it == null) return@addTextChangedListener
            _counter.value = if (it.isEmpty()) 0 else it.toString().toInt()
        }
        decreaseButton.setOnClickListener {
            _counter.value = max(_counter.value - 1, 0)
        }
        increaseButton.setOnClickListener {
            _counter.value = _counter.value + 1
        }
        playPauseButton.setOnClickListener {
            _isPlay.value = !isPlay.value
        }
    }

    private fun observeObservables() {
        val factory = Editable.Factory()
        counter.addObserver {
            if (it.toString() == binding.numberEditText.text.toString()) return@addObserver
            binding.numberEditText.text = factory.newEditable("$it")
        }
        isPlay.addObserver {
            binding.playPauseButton.text = getString(if (it) R.string.stop else R.string.play)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _counter.releaseObservers()
        _isPlay.releaseObservers()
    }
}
