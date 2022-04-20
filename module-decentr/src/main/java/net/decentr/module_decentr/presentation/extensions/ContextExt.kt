package net.decentr.module_decentr.presentation.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import net.decentr.module_decentr.R

fun Context.copyToClipboard(text: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clipData = ClipData.newPlainText("text", text)
    Toast.makeText(this, getString(R.string.copied_to_clipboard_toast), Toast.LENGTH_SHORT).show()
    clipboardManager?.setPrimaryClip(clipData)
}