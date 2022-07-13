package net.decentr.module_decentr_staking.remote.mapper

import cosmos.distribution.v1beta1.Distribution
import net.decentr.module_decentr_domain.models.staking.DecCoin
import net.decentr.module_decentr_domain.models.staking.Reward


fun Distribution.DelegationDelegatorReward.toDomain() = Reward(
    validatorAddress = this.validatorAddress,
    decCoin = DecCoin(
        amount = this.rewardList.sumOf { it.amount.toBigDecimal().divide((DIVIDER_BIG).toBigDecimal()).divide((DIVIDER_MEDIUM).toBigDecimal()) }.toString(),
        denom = this.rewardList.first().denom
    )
)