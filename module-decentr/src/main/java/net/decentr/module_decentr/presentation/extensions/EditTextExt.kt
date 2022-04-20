package net.decentr.module_decentr.presentation.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText?.string(): String {
    return this?.text?.toString() ?: ""
}

fun EditText?.onTextChanged(sequence: (CharSequence?) -> Unit) {
    this?.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            sequence(s)
        }
    })
}