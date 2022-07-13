package net.decentr.module_decentr_staking.presentation.base.adapter.recycler

import androidx.annotation.IdRes

interface DraggableItem {
    @IdRes
    fun getDraggableViewId(): Int
}