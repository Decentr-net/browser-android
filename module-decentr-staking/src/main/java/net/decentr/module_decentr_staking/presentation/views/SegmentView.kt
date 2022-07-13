package net.decentr.module_decentr_staking.presentation.views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Checkable
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import net.decentr.module_decentr_staking.R
import net.decentr.module_decentr_staking.presentation.extensions.inflateLayout
import net.decentr.module_decentr_staking.presentation.extensions.toPx
import net.decentr.module_decentr_staking.presentation.extensions.views


private const val MARGIN_HORIZONTAL = 10 //dp
private const val MARGIN_VERTICAL = 3 //dp
private const val MARGIN_SPACE = 5 //dp
private const val MAX_LINES = 2
private const val ELEVATION = 2//dp
private const val MAX_WIDTH = 170 //dp

class SegmentButtonsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs), View.OnClickListener {


    private var values = listOf<Segment>()
    private var showSettings = true
    var selectedPosition = -1
        private set

    private val container: LinearLayout

    init {
        context.inflateLayout(R.layout.view_segment_buttons, this, true)
        container = findViewById(R.id.container)
    }

    var listener: ((segment: Segment, v: View) -> Unit)? = null

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (!enabled) {
            showSettings = false
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setPadding(paddingLeft, 0, paddingLeft, 0)
    }

    fun setValues(values: List<Segment>) {
        if (this.values == values) {
            return
        }
        this.values = values
        container.removeAllViews()
        values.forEach { segment ->
            addItem(SegmentView(context).apply {
                text = segment.text
                tag = segment
                isEnabled = this@SegmentButtonsView.isEnabled
                if (this@SegmentButtonsView.isEnabled) {
                    setOnClickListener(this@SegmentButtonsView)
                }
                isChecked = segment.isSelected
            })
        }
    }

    override fun onClick(v: View?) {
        if (!isEnabled) {
            return
        }
        container.views.forEachIndexed { index, view ->
            if (view is Checkable) {
                if (view == v) {
                    if (!view.isChecked) {
                        view.isChecked = true
                        selectedPosition = index
                        listener?.invoke(view.tag as Segment, view)
                    }
                } else {
                    view.isChecked = false
                }
            }
        }
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        container.views.forEachIndexed { index, view ->
            if (view is Checkable) {
                view.isChecked = index == position
            }
        }
    }

    private fun addItem(view: View) {
        container.addView(view)
    }

    private class SegmentView(context: Context) : AppCompatTextView(context), Checkable {

        init {
            gravity = Gravity.CENTER
            isClickable = true
            ellipsize = TextUtils.TruncateAt.END
            isFocusable = true
            maxLines = MAX_LINES
            maxWidth = MAX_WIDTH.toPx()
            TextViewCompat.setTextAppearance(this, R.style.Base_Regular_Secondary)
            background = null
            elevation = 0f

            val param = LinearLayout.LayoutParams(
                0,
                LayoutParams.MATCH_PARENT,
                1.0f
            )

            layoutParams = param
            setPadding(
                0,
                MARGIN_SPACE.toPx(),
                0,
                MARGIN_SPACE.toPx()
            )
        }

        override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            (layoutParams as? MarginLayoutParams)?.setMargins(
                MARGIN_SPACE.toPx(),
                MARGIN_VERTICAL.toPx(),
                MARGIN_SPACE.toPx(),
                MARGIN_VERTICAL.toPx()
            )
        }

        private var isChecked = false


        override fun isChecked(): Boolean {
            return isChecked
        }

        override fun toggle() {
            setChecked(!isChecked)
        }

        override fun setChecked(checked: Boolean) {
            isChecked = checked
            if (checked) {
                alpha = 1f
                TextViewCompat.setTextAppearance(this, R.style.Base_Regular_Primary)
                setBackgroundResource(R.drawable.shape_bg_base_rounded)
                elevation = ELEVATION.toPx().toFloat()
            } else {
                if (isEnabled) {
                    alpha = 1f
                    TextViewCompat.setTextAppearance(this, R.style.Base_Regular_Primary)
                    background = null
                    elevation = 0f
                } else {
                    alpha = 0.5f
                    TextViewCompat.setTextAppearance(this, R.style.Base_Regular_Secondary)
                    background = null
                    elevation = 0f
                }
            }
        }

    }
}