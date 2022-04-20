package net.decentr.module_decentr.presentation.profile.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import net.decentr.module_decentr.R
import net.decentr.module_decentr.presentation.extensions.toPx


private const val PADDING_VERTICAL = 6 //dp
private const val PADDING_HORIZONTAL = 10 //dp
private const val MARGIN_SPACE = 2 //dp

class DropdownStringView(context: Context) : AppCompatSpinner(context) {

    var values: List<String>? = null
    var selectedItemListener: ((value: String, v: View) -> Unit)? =
        null

    init {
        isClickable = true
        isFocusable = true
        setBackgroundResource(R.drawable.shape_bg_button_base)
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        setPadding(
            PADDING_HORIZONTAL.toPx(),
            PADDING_VERTICAL.toPx(),
            PADDING_HORIZONTAL.toPx(),
            PADDING_VERTICAL.toPx()
        )
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (layoutParams as? MarginLayoutParams)?.setMargins(
            0,
            MARGIN_SPACE.toPx(),
            0,
            MARGIN_SPACE.toPx()
        )
    }

    fun setListValues(values: List<String>, hintString: String) {
        this.values = values
        val itemsWithHint = mutableListOf<String>()
        itemsWithHint.add(hintString)
        itemsWithHint.addAll(values)
        val spinnerAdapter = object : ArrayAdapter<String>(
            context, R.layout.item_dropdown_gender,
            itemsWithHint
        ) {

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView =
                    super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(ContextCompat.getColor(context, R.color.dec_grey))
                } else {
                    view.setTextColor(ContextCompat.getColor(context, R.color.dec_black))
                }
                return view
            }
        }
        spinnerAdapter.setDropDownViewResource(R.layout.item_dropdown_gender)
        onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent?.getItemAtPosition(position).toString()
                if (value == itemsWithHint[0]) {
                    (view as TextView).setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.dec_grey
                        )
                    )
                } else {
                    (view as TextView).setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.dec_black
                        )
                    )
                    selectedItemListener?.invoke(value, view)
                }
            }
        }
        this@DropdownStringView.adapter = spinnerAdapter

    }
}