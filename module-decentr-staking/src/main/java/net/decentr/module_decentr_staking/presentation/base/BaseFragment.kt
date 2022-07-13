package net.decentr.module_decentr_staking.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment @JvmOverloads constructor(@LayoutRes contentLayoutId: Int = 0) :
    Fragment(contentLayoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.invalidateOptionsMenu()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(this is SearchView.OnQueryTextListener)
        activity?.onBackPressedDispatcher?.addCallback(
            this, object: OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    view?.findNavController()?.popBackStack()
                }
            }
        )
    }

    protected fun setTitle(titleResId: Int) {
        activity?.setTitle(titleResId)
    }

    protected fun setTitle(charSequence: CharSequence?) {
        activity?.title = charSequence
    }

    protected fun navigateTo(direction: NavDirections) {
        view?.findNavController()?.navigate(direction)

    }

    protected fun back() {
        view?.findNavController()?.navigateUp()
    }
}