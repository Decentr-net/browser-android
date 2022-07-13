package net.decentr.module_decentr_staking.presentation.staking.viewstates

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WithdrawResultViewState(
    val decAmount: String
): Parcelable