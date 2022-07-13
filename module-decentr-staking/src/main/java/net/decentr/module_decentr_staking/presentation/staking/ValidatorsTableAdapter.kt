package net.decentr.module_decentr_staking.presentation.staking

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import net.decentr.module_decentr_staking.R
import net.decentr.module_decentr_staking.presentation.extensions.toPx
import net.decentr.module_decentr_staking.presentation.staking.viewstates.ValidatorViewState
import net.decentr.module_decentr_staking.presentation.views.tablefixheaders.adapters.BaseTableAdapter
import kotlin.math.roundToInt

class ValidatorsTableAdapter<T> @JvmOverloads constructor(
    private val context: Context,
    table: Array<ValidatorViewState>? = null,
    private val listener: ((item: ValidatorViewState) -> Unit)? = null
) : BaseTableAdapter() {

    private var table: Array<ValidatorViewState>? = null
    private val width: Int
    private val height: Int

    fun setInformation(table: Array<ValidatorViewState>?) {
        this.table = null
        val tableWithEmptyVSForHeaders = mutableListOf(ValidatorViewState(String(), String(), false, String(), String(), ValidatorViewState.Status.UNSPECIFIED, String(), String(), String(), String()))
        table?.toList()?.let { tableWithEmptyVSForHeaders.addAll(it) }
        this.table = tableWithEmptyVSForHeaders.toTypedArray()
    }

    override fun getRowCount(): Int {
        return table!!.size - 1
    }

    override fun getColumnCount(): Int {
        return 4
    }

    override fun getView(row: Int, column: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = ItemView(context)
        }
        val text = when (row) {
            -1 -> {
                when (column) {
                    VALIDATORS_COLUMN_INDEX -> context.getString(R.string.header_staking_validator)
                    VOTING_POWER_COLUMN_INDEX -> context.getString(R.string.header_staking_voting_power)
                    COMISSION_COLUMN_INDEX -> context.getString(R.string.header_staking_commision)
                    DELEGATED_COLUMN_INDEX -> context.getString(R.string.header_staking_dec_amount)
                    else -> ""
                }
            }
            else -> {
                when (column) {
                    VALIDATORS_COLUMN_INDEX -> table?.get(row + 1)?.name
                    VOTING_POWER_COLUMN_INDEX -> table?.get(row + 1)?.votingPower
                    COMISSION_COLUMN_INDEX -> (table?.get(row + 1)?.commission + "%")
                    DELEGATED_COLUMN_INDEX -> table?.get(row + 1)?.delegatedAmount
                    else -> ""
                }
            }
        }
        (convertView as ItemView).apply {
            if (column == STATUS_COLUMN_INDEX) {
                if (row == -1) {
                    this.setText(context.getString(R.string.header_staking_status), true)
                } else {
                    val image = when (table?.get(row + 1)?.status) {
                        ValidatorViewState.Status.UNBONDED -> ContextCompat.getDrawable(context, R.drawable.ic_status_unbonded)
                        ValidatorViewState.Status.UNBONDING -> ContextCompat.getDrawable(context, R.drawable.ic_status_unbonding)
                        ValidatorViewState.Status.BONDED -> ContextCompat.getDrawable(context, R.drawable.ic_status_bonded)
                        ValidatorViewState.Status.UNSPECIFIED,
                        null -> null
                    }
                    this.setImage(image)
                }
            } else {
                this.setText(text, row == -1)
            }
            this.tag = table?.get(row + 1)
        }
        if (row != -1) {
            convertView.setOnClickListener {
                (((convertView as? ItemView)?.tag) as? ValidatorViewState)?.let {
                    listener?.invoke(it)
                }
            }
        }
        return convertView
    }

    override fun getHeight(row: Int): Int {
        return height
    }

    override fun getWidth(column: Int): Int {
        return when (column) {
            VALIDATORS_COLUMN_INDEX -> (VALIDATORS_COLUMN_WIDTH).toPx()
            VOTING_POWER_COLUMN_INDEX -> (VOTING_POWER_COLUMN_WIDTH).toPx()
            STATUS_COLUMN_INDEX -> (STATUS_COLUMN_WIDTH).toPx()
            COMISSION_COLUMN_INDEX -> (COMISSION_COLUMN_WIDTH).toPx()
            DELEGATED_COLUMN_INDEX -> (DELEGATED_COLUMN_WIDTH).toPx()
            else -> width
        }
    }

    override fun getItemViewType(row: Int, column: Int): Int {
        return 0
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    companion object {
        private const val WIDTH_DIP = 110
        private const val HEIGHT_DIP = 32

        private const val VALIDATORS_COLUMN_INDEX = -1
        private const val VOTING_POWER_COLUMN_INDEX = 0
        private const val STATUS_COLUMN_INDEX = 1
        private const val COMISSION_COLUMN_INDEX = 2
        private const val DELEGATED_COLUMN_INDEX = 3

        private const val VALIDATORS_COLUMN_WIDTH = 110
        private const val VOTING_POWER_COLUMN_WIDTH = 190
        private const val STATUS_COLUMN_WIDTH = 70
        private const val COMISSION_COLUMN_WIDTH = 100
        private const val DELEGATED_COLUMN_WIDTH = 90
    }

    init {
        val r = context.resources
        width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            WIDTH_DIP.toFloat(),
            r.displayMetrics
        ).roundToInt()
        height = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            HEIGHT_DIP.toFloat(),
            r.displayMetrics
        ).roundToInt()
        setInformation(table)
    }

    private class ItemView(
        context: Context
    ) : LinearLayout(context) {

        private val labelTextView =
            TextView(
                ContextThemeWrapper(
                    context,
                    R.style.Base_Regular_Primary
                )
            ).apply {
                setPadding((2).toPx(), (2).toPx(), (2).toPx(), (2).toPx())
                maxLines = 1
                setSingleLine()
                ellipsize = TextUtils.TruncateAt.END
            }

        private val labelImageView = AppCompatImageView(context)

        init {
            orientation = VERTICAL
            gravity = Gravity.START
            minimumHeight = (14).toPx()
        }

        fun setText(labelString: String?, isHeader: Boolean = false) {
            removeAllViews()
            labelTextView.text = labelString
            if (isHeader) {
                TextViewCompat.setTextAppearance(labelTextView, R.style.Small_Regular_Secondary)
            } else {
                TextViewCompat.setTextAppearance(labelTextView, R.style.Base_Regular_Primary)
            }
            addView(labelTextView)
            addView(View(context).apply {
                layoutParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, (1).toPx())
                setBackgroundColor(ContextCompat.getColor(context, R.color.dec_outlines))
            })
        }

        fun setImage(labelImg: Drawable?) {
            removeAllViews()
            addView(
                labelImageView.apply {
                    setImageDrawable(labelImg)
                }, LayoutParams(
                    (24).toPx(),
                    (20).toPx()
                ).apply {
                    setMargins(
                        (3).toPx(),
                        (3).toPx(),
                        (3).toPx(),
                        (3).toPx()
                    )
                })
            addView(View(context).apply {
                layoutParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, (1).toPx())
                setBackgroundColor(ContextCompat.getColor(context, R.color.dec_outlines))
            })
        }

    }
}