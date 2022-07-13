package net.decentr.module_decentr_staking.presentation.staking.viewstates

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidatorViewState(
    val address: String,
    val name: String,
    val isJailed: Boolean,
    val votingPower: String,
    val votingPowerPercent: String,
    val status: Status,
    val commission: String,
    val delegatedAmount: String,
    val website: String?,
    val description: String?
): Parcelable {

    enum class Status {
        UNSPECIFIED,
        UNBONDED,
        UNBONDING,
        BONDED
    }
}