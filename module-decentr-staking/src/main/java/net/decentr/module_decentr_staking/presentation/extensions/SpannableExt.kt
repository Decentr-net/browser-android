package net.decentr.module_decentr_staking.presentation.extensions

import android.content.Context
import android.text.Spannable
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes


fun Spannable.textAppearance(context: Context?, @StyleRes style: Int, subString: String) {
    val startPosition = indexOf(subString)
    val endPosition = startPosition + subString.length

    if (startPosition < 0 || endPosition > length) {
        return
    }
    setSpan(TextAppearanceSpan(context, style), startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

fun Spannable.textForegroundColor(@ColorInt color: Int, subString: String) {
    val startPosition = indexOf(subString)
    val endPosition = startPosition + subString.length

    if (startPosition < 0 || endPosition > length) {
        return
    }
    setSpan(ForegroundColorSpan(color), startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

fun Spannable.textUnderline(subString: String) {
    val startPosition = indexOf(subString)
    val endPosition = startPosition + subString.length

    if (startPosition < 0 || endPosition > length) {
        return
    }
    setSpan(UnderlineSpan(), startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

fun Spannable.textClick(subString: String, action: () -> Unit) {
    val startPosition = indexOf(subString)
    val endPosition = startPosition + subString.length
    val clickableSpan =
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    action()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }

    if (startPosition < 0 || endPosition > length) {
        return
    }
    setSpan(clickableSpan, startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}