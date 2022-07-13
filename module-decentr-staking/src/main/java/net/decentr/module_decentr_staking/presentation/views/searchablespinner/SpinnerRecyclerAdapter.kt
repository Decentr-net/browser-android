package net.decentr.module_decentr_staking.presentation.views.searchablespinner

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import net.decentr.module_decentr_staking.R

internal class SpinnerRecyclerAdapter(
    private val context: Context,
    private val list: List<SpinnerViewState>,
    private val onItemSelectListener: OnItemSelectListener
) :
    RecyclerView.Adapter<SpinnerRecyclerAdapter.SpinnerHolder>() {

    private var spinnerListItems: List<SpinnerViewState> = list
    private lateinit var selectedItem: SpinnerViewState
    var highlightSelectedItem: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinnerHolder {
        return SpinnerHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_seachable_spinner,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return spinnerListItems.size
    }

    override fun onBindViewHolder(holder: SpinnerHolder, position: Int) {
        val currentViewState = spinnerListItems[position]
        holder.textPrimarySpinnerItem.text = currentViewState.primaryText
        holder.textSecondarySpinnerItem.text = currentViewState.secondaryText ?: String()
        holder.textPrimarySpinnerItem.setOnClickListener {
            holder.textPrimarySpinnerItem.isClickable = false
            selectedItem = currentViewState
            onItemSelectListener.setOnItemSelectListener(
                getOriginalItemPosition(currentViewState),
                currentViewState
            )
        }
    }


    class SpinnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textPrimarySpinnerItem: TextView = itemView.findViewById(R.id.text_primary)
        val textSecondarySpinnerItem: TextView = itemView.findViewById(R.id.text_secondary)
    }

    fun filter(query: CharSequence?) {
        val filteredNames: ArrayList<SpinnerViewState> = ArrayList()
        if (query.isNullOrEmpty()) {
            filterList(list)
        } else {
            for (item in list) {
                if (item.primaryText.contains(query, true)) {
                    filteredNames.add(item)
                }
            }
            filterList(filteredNames)
        }
    }

    private fun filterList(filteredNames: List<SpinnerViewState>) {
        spinnerListItems = filteredNames
        notifyDataSetChanged()
    }

    private fun getOriginalItemPosition(selectedString: SpinnerViewState): Int {
        return list.indexOf(selectedString)
    }
}