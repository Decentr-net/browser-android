package net.decentr.module_decentr.presentation.login.views.otpview

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import net.decentr.module_decentr.R

internal class OTPChildEditText : androidx.appcompat.widget.AppCompatEditText {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
//        minHeight = 80.toPx()
        isCursorVisible = false
        setTextColor(ContextCompat.getColor(context, R.color.transparent))
        background = null
        inputType = InputType.TYPE_CLASS_TEXT
        setSelectAllOnFocus(false)
        setTextIsSelectable(false)
    }

    public override fun onSelectionChanged(start: Int, end: Int) {
        val text = text
        text?.let {
            if (start != it.length || end != it.length) {
                setSelection(it.length, it.length)
                return
            }
        }

        super.onSelectionChanged(start, end)
    }

}