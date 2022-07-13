package net.decentr.module_decentr_staking.presentation.extensions

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Number?.format(): String {
    this?.let {
        val fmt = DecimalFormat().apply {
            groupingSize = 3
            isGroupingUsed = true
            decimalFormatSymbols = DecimalFormatSymbols().apply {
                groupingSeparator = ' '
            }
        }
        return fmt.format(this)
    } ?: return "null"
}