package net.decentr.module_decentr_staking.presentation.views

data class Segment(
    val id: Int,
    val text: String,
    val isSelected: Boolean = false,
    val isEnabled: Boolean = true
)