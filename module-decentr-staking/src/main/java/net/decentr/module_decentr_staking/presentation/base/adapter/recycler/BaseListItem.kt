package net.decentr.module_decentr_staking.presentation.base.adapter.recycler

import android.view.View
import androidx.annotation.LayoutRes


/**
 * Base interface for all items in application's RecyclerViews.
 * Must be used with BaseRecyclerAdapter.
 */
abstract class BaseListItem<VS : Any>(val viewState: VS) {

    @LayoutRes
    abstract fun getViewId(): Int

    abstract fun renderView(view: View, positionInAdapter: Int = 0)

    open fun payloadView(view: View, positionInAdapter: Int, payloads: MutableList<Any>) {
    }

    open fun recycleView(view: View?) {
    }

    open fun areItemsTheSame(oldItem: BaseListItem<VS>?): Boolean {
        return viewState == oldItem?.viewState
    }

    open fun areContentsTheSame(oldItem: BaseListItem<VS>?): Boolean {
        return viewState == oldItem?.viewState
    }

    open fun getChangePayload(oldItem: BaseListItem<VS>?): Any? {
        return null
    }
}