package net.decentr.module_decentr.presentation.base

import androidx.appcompat.app.ActionBar

interface NavHostActivity {

    /**
     * Returns the support action bar, inflating it if necessary.
     * Everyone should call this instead of supportActionBar.
     */
    fun getSupportActionBarAndInflateIfNecessary(): ActionBar
}