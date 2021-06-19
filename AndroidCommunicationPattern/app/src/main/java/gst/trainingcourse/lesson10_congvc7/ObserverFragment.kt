package gst.trainingcourse.lesson10_congvc7

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import gst.trainingcourse.lesson10_congvc7.core.Observable
import gst.trainingcourse.lesson10_congvc7.core.Observer
import gst.trainingcourse.lesson10_congvc7.core.transform
import gst.trainingcourse.lesson10_congvc7.databinding.FragmentObservableBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.yield

private const val DELAY_INTERVAL = 1_000L

sealed class ObserverFragment : ViewBindingFragment<FragmentObservableBinding>() {

    protected abstract val predicate: suspend (Int) -> Boolean

    final override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentObservableBinding.inflate(inflater, container, false)

    // Note: call after host activity created
    private lateinit var transformedPolicy: Observable<Flow<Int>>

    @Volatile
    private var isPlayLock = false
    private val isPlayObserver = Observer<Boolean> {
        isPlayLock = it
        if (!it) bind(null)
    }

    private val flowObserver by lazy {
        val lock = Any()
        var job: Job? = null
        Observer<Flow<Int>> {
            job?.cancel()
            synchronized(lock) {
                job = lifecycleScope.launchWhenStarted {
                    while (true) {
                        it.takeWhile { isPlayLock }
                            .collect {
                                bind(it)
                                delay(DELAY_INTERVAL)
                            }
                        yield()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireNotNull(activity as? MainActivityContract) {
            "Host activity must implement MainActivityContract"
        }.let {
            it.isPlay.addObserver(isPlayObserver)
            transformedPolicy = it.counter.transform(emptyFlow(), lifecycleScope) { i: Int ->
                emit((0..i).asFlow().filter(predicate))
            }
        }
        transformedPolicy.addObserver(flowObserver)
    }

    override fun onDestroyView() {
        (activity as? MainActivityContract)?.isPlay?.removeObserver(isPlayObserver)
        transformedPolicy.removeObserver(flowObserver)
        super.onDestroyView()
    }

    @MainThread
    private fun bind(value: Int?) = with(binding) {
        numberTextView.text = value?.toString() ?: ""

        if (value == null) {
            numberTextView.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_border_thin)
            return
        }
        numberTextView.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (value % 2 == 0) R.color.black else R.color.white
            )
        )
        numberTextView.setBackgroundColor(if (value % 2 == 0) Color.RED else Color.BLUE)
    }
}

class FirstFragment : ObserverFragment() {
    override val predicate: suspend (Int) -> Boolean = { it % 2 != 0 }
}

class SecondFragment : ObserverFragment() {
    override val predicate: suspend (Int) -> Boolean = { it % 2 == 0 }
}

class ThirdFragment : ObserverFragment() {
    override val predicate: suspend (Int) -> Boolean = { it % 3 == 0 }
}

class FourthFragment : ObserverFragment() {
    override val predicate: suspend (Int) -> Boolean = { it % 5 == 0 }
}
