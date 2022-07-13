package net.decentr.module_decentr_staking.presentation.staking.viewstates

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RewardsViewState

@Parcelize
data class RewardViewState(
    val validatorName: String,
    val delegated: String,
    var rewardAmount: String,
    val validatorAddress: String,
    var isChecked: Boolean = false
): RewardsViewState(), Parcelable

@Parcelize
data class RewardLegendViewState(
    val withdraw: String,
    val fee: String
): RewardsViewState(), Parcelable