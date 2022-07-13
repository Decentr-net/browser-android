package net.decentr.module_decentr_domain.models.staking

data class Reward(
    val validatorAddress: String,
    val decCoin: DecCoin
)