package net.decentr.module_decentr_staking.presentation.base.adapter.recycler

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import net.decentr.module_decentr_staking.presentation.extensions.inflateLayout
import java.util.*

/**
 * Base recycler for all recyclerViews
 */

class BaseRecyclerAdapter<VS : Any>(
    val mapper: (VS) -> BaseListItem<VS>,
    private var onItemClickListener: ((item: VS, view: View, position: Int) -> Unit)? = null,
    private var onItemLongClickListener: ((item: VS, view: View, position: Int) -> Unit)? = null) : RecyclerView.Adapter<BaseViewHolder<VS>>() {

    companion object {
        const val HOLDER_TAG_KEY = 0x03000285
    }

    val dataset = mutableListOf<BaseListItem<VS>>()

    private var holderItemTouchHelper: ItemTouchHelper? = null

    override fun onBindViewHolder(holder: BaseViewHolder<VS>, position: Int) {
        holder.renderItem(dataset[position])
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VS>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            holder.payloadItem(dataset[position], payloads)
        }
    }

    override fun getItemCount(): Int = dataset.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VS> {
        val view = parent.context.inflateLayout(viewType, parent, false)
        val holder = BaseViewHolder(
            view = view,
            onItemClickListener = onItemClickListener,
            onItemLongClickListener = onItemLongClickListener,
            itemTouchHelper = holderItemTouchHelper
        )
        view.setTag(HOLDER_TAG_KEY, holder)
        return holder
    }

    override fun getItemViewType(position: Int): Int = dataset[position].getViewId()

    override fun onViewRecycled(holder: BaseViewHolder<VS>) {
        holder.onViewRecycled()
    }

    fun getItem(position: Int) = dataset[position]

    fun addElements(newElements: Collection<VS>) {
        dataset.addAll(newElements.map(mapper))
    }

    fun replaceElements(newElements: List<VS>) {
        dataset.clear()
        dataset.addAll(newElements.map(mapper))
    }

    fun replaceElementsWithDiffUtil(items: List<VS>, enableAnimation: Boolean = true) {
        val diffUtilCallback = DiffUtilCallback(dataset.toList(), items.map(mapper))
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, enableAnimation)
        replaceElements(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun replaceElement(vs: VS, position: Int): BaseListItem<VS>? {
        if (position < dataset.size) {
            val item = mapper(vs)
            dataset.removeAt(position)
            dataset.add(position, item)
            return item
        }
        return null
    }

    fun addElement(item: VS) {
        dataset.add(mapper(item))
    }

    fun addElement(position: Int, item: VS) {
        dataset.add(position, mapper(item))
    }

    fun addElements(position: Int, items: List<VS>) {
        dataset.addAll(position, items.map(mapper))
    }

    fun addElementsAnimated(pivotItem: VS, items: List<VS>) {
        val pivotPosition = dataset.indexOf(mapper(pivotItem))
        if (pivotPosition >= 0) {
            val startPosition = pivotPosition + 1
            addElements(startPosition, items)
            notifyItemRangeInserted(startPosition, items.size)
        }
    }

    fun removeElement(item: VS) {
        dataset.remove(mapper(item))
    }

    fun removeElements(items: List<VS>) {
        dataset.removeAll(items.map(mapper))
    }

    fun removeElementByPosition(position: Int) {
        if (dataset.size > position) {
            dataset.removeAt(position)
        }
    }

    fun clear() {
        dataset.clear()
    }

    fun deleteElementsAnimated(vararg items: VS) {
        deleteElementsAnimated(listOf(*items))
    }

    fun deleteElementsAnimated(items: List<VS>) {
        val findPos = ArrayList<Int>()
        val findItems = ArrayList<VS>()
        for (item in items) {
            val position = dataset.map { it.viewState }.indexOf(item)
            if (position >= 0) {
                findPos.add(position)
                findItems.add(item)
            }
        }
        val size = findPos.size
        if (size > 0) {
            removeElements(findItems)
            val startPos = findPos[0]
            val endPos = findPos[findPos.size - 1]
            //try to notify range
            if (endPos - startPos == findPos.size - 1) {
                notifyItemRangeRemoved(startPos, findPos.size)
            } else {
                // notify
                for (pos in findPos) {
                    notifyItemRemoved(pos)
                }
            }
        }
    }

    fun setItemTouchHelper(itemTouchHelper: ItemTouchHelper?) {
        holderItemTouchHelper = itemTouchHelper
    }

    fun findPosition(predicate: (VS) -> Boolean): Int {
        return dataset.indexOfFirst {
            predicate(it.viewState)
        }
    }

    private class DiffUtilCallback<VS : Any>(private var oldList: List<BaseListItem<VS>>,
                                             private var newList: List<BaseListItem<VS>>) : DiffUtil.Callback() {

        /*
        * Returns the size of the old list.
        */
        override fun getOldListSize(): Int = oldList.size

        /*
        * Returns the size of the new list.
        */
        override fun getNewListSize(): Int = newList.size

        /*
        * Called by the DiffUtil to decide whether two object represent the same Item.
        */
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return newItem.areItemsTheSame(oldItem)
        }

        /*
        * Called by the DiffUtil when it wants to check whether two items have the same data.
        * DiffUtil uses this information to detect if the contents of an item has changed.
        */
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return newItem.areContentsTheSame(oldItem)
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return newItem.getChangePayload(oldItem)
        }
    }

}