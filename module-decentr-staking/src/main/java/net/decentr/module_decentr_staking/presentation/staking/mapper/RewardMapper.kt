package net.decentr.module_decentr_staking.presentation.staking.mapper

import net.decentr.module_decentr_domain.models.staking.Reward
import net.decentr.module_decentr_staking.presentation.extensions.removeEndingZeros
import net.decentr.module_decentr_staking.presentation.staking.viewstates.RewardViewState
import java.text.DecimalFormat

fun Reward.toViewState(validatorName: String, delegated: String) = RewardViewState(
    validatorName = validatorName,
    delegated = delegated.let {
        val value = it.toDouble()
        val decimalFormat = DecimalFormat("0.000")
        decimalFormat.format(value).replace(',', '.').removeEndingZeros().trimEnd('.')
    },
    rewardAmount = this.decCoin.amount.let {
        val value = it.toDouble()
        val decimalFormat = DecimalFormat("0.00000")
        decimalFormat.format(value).replace(',', '.').removeEndingZeros().trimEnd('.')
    },
    validatorAddress = this.validatorAddress
)