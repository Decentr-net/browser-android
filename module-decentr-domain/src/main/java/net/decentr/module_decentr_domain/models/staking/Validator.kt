package net.decentr.module_decentr_domain.models.staking

import java.util.*

data class Validator(
    val name: String,
    val address: String,
    val jailed: Boolean,
    val status: Status,
    val tokens: String?,
    val delegatorShares: String,
    val description: String?,
    val unbondingTime: Date?,
    val comission: Comission,
    val minSelfDelegation: String,
    val website: String?
) {
    enum class Status(val value: Int) {
        UNSPECIFIED(0),
        UNBONDED(1),
        UNBONDING(2),
        BONDED(3)
    }

    data class Comission(
        val rates: ComissionRate,
        val updatedTime: Date
    )

    data class ComissionRate(
        val rate: String,
        val maxRate: String,
        val maxChangeRate: String

    )
}