package gst.trainingcourse.advancedlesson78_congvc7.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

abstract class ViewBindingFragment<out T : ViewBinding> : Fragment(), LifecycleAwareFlowCollector {
    private var _binding: T? = null

    /**
     * Available after [Fragment.onCreateView] returns
     * and before [Fragment.onDestroyView]'s super is called
     */
    protected val binding: T
        get() = _binding ?: throw IllegalStateException(
            "binding is only valid between onCreateView and onDestroyView"
        )

    /**
     * Called in [Fragment.onCreateView] to get the rootView of [binding]
     */
    protected abstract val inflateRef: (LayoutInflater, ViewGroup?, Boolean) -> T

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflateRef(inflater, container, false)
        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun observeLiveData() = Unit

    final override fun <T> Flow<T>.collectWhenStarted(action: suspend (T) -> Unit): Job {
        return lifecycleScope.launchWhenStarted { collect(action) }
    }
}
