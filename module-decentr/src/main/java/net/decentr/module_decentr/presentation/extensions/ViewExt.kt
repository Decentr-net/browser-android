package net.decentr.module_decentr.presentation.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun View.show(show: Boolean, invisible: Boolean = false) {
    visibility = if (show) View.VISIBLE else if (invisible) View.INVISIBLE else View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View?.hideKeyboard() {
    this?.let {
        postDelayed({
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }, 100)
    }
}

fun hideViewIfNotFits(viewToHide: View, viewToFit: View) {
    with(Rect()) {
        viewToFit.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewToFit.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (!(viewToFit.getGlobalVisibleRect(this@with)
                            && viewToFit.height == this@with.height() && viewToFit.width == this@with.width())) {
                    viewToHide.gone()
                }
            }
        })
    }
}

inline val ViewGroup.views
    get() = (0 until childCount).map { getChildAt(it) }
