package gst.trainingcourse.advancedlesson78_congvc7.ui.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
//
// fun <F : Fragment, V : ViewDataBinding> F.dataBiding(@LayoutRes res: Int): ReadOnlyProperty<F, V> {
//     return DefaultViewBindingFragmentDelegate(viewLifecycleOwner,
//     DataBindingUtil.inflate<>())
// }

private class DefaultViewBindingFragmentDelegate<UIController, V : ViewBinding>(
    private val viewLifecycleOwner: LifecycleOwner,
    private val initializer: () -> V,
) : ReadOnlyProperty<UIController, V>, DefaultLifecycleObserver {
    private var binding: V? = null

    init {
        viewLifecycleOwner.lifecycle.addObserver(this)
    }

    override fun getValue(thisRef: UIController, property: KProperty<*>): V {
        check(viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            "Access binding component in state: ${viewLifecycleOwner.lifecycle.currentState}"
        }
        return binding!!
    }

    override fun onCreate(owner: LifecycleOwner) {
        synchronized(this) {
            if (binding == null) {
                binding = initializer()
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        binding = null
    }
}
