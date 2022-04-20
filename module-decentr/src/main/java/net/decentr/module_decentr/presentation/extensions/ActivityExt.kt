package net.decentr.module_decentr.presentation.extensions

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity

fun Activity.setNavigationIcon(
    @DrawableRes icon: Int
) {
    (this as? AppCompatActivity)?.supportActionBar?.let {
        it.setDisplayHomeAsUpEnabled(true)
        it.setHomeAsUpIndicator(icon)
        it.setHomeActionContentDescription("Navigation up")
    }
}