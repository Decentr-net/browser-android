package net.decentr.module_decentr_staking.presentation.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> FragmentActivity.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory)[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory)[T::class.java]
}

fun Fragment.toast(text: CharSequence?) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

inline fun Fragment.runIfInitialized(func: () -> Unit) {
    if (!isDetached && (view != null) && viewLifecycleOwner.lifecycle.currentState.isAtLeast(
            Lifecycle.State.CREATED)) {
        func()
    }
}

inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
    provider: ViewModelProvider.Factory
) = ViewModelProvider(this, provider)[VM::class.java]