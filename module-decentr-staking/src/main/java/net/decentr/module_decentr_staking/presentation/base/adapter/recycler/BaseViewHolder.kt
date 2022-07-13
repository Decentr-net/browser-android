package net.decentr.module_decentr_staking.presentation.base.adapter.recycler

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import net.decentr.module_decentr_staking.presentation.extensions.isAvailable


class BaseViewHolder<VS : Any>(view: View,
                               private val onItemClickListener: ((item: VS, view: View, position: Int) -> Unit)? = null,
                               private val onItemLongClickListener: ((item: VS, view: View, position: Int) -> Unit)? = null,
                               private val itemTouchHelper: ItemTouchHelper? = null)
    : RecyclerView.ViewHolder(view) {

    var item: BaseListItem<VS>? = null

    fun renderItem(holderItem: BaseListItem<VS>?) {
        item = holderItem
        holderItem?.let {
            it.renderView(itemView, absoluteAdapterPosition)
            setClickListeners(itemView, it)
            setItemTouchHelper(itemView, holderItem)
        }
    }

    fun payloadItem(holderItem: BaseListItem<VS>?, payloads: MutableList<Any>) {
        item = holderItem
        holderItem?.let {
            it.payloadView(itemView, absoluteAdapterPosition, payloads)
            setClickListeners(itemView, it)
            setItemTouchHelper(itemView, holderItem)
        }
    }

    fun onViewRecycled() {
        if (itemView.context.isAvailable()) {
            item?.recycleView(itemView)
        }
    }

    fun selectItem(isSelected: Boolean) {
        (item as? SelectableItem)?.selectItem(isSelected)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClickListeners(view: View?, holderItem: BaseListItem<VS>) {
        onItemClickListener?.let {
            view?.setOnClickListener {
                it(holderItem.viewState, view, absoluteAdapterPosition)
            }
        }
        onItemLongClickListener?.let {
            view?.setOnLongClickListener {
                it(holderItem.viewState, view, absoluteAdapterPosition)
                true
            }
        }

        view?.setOnLongClickListener {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.invoke(holderItem.viewState, view, absoluteAdapterPosition)
                return@setOnLongClickListener true
            }
            return@setOnLongClickListener false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setItemTouchHelper(view: View?, holderItem: BaseListItem<VS>) {
        if (itemTouchHelper != null && holderItem is DraggableItem) {
            val v: View? = view?.findViewById(holderItem.getDraggableViewId())
            v?.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN)
                    itemTouchHelper.startDrag(this)
                return@setOnTouchListener false
            }
        }
    }

}