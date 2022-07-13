package net.decentr.module_decentr_staking.presentation.extensions

fun String.removeEndingZeros(): String {
    return this.trimEnd('0')
}