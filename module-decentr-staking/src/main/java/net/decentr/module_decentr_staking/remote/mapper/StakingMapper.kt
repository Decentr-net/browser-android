package net.decentr.module_decentr_staking.remote.mapper

import cosmos.staking.v1beta1.Staking
import net.decentr.module_decentr_domain.models.staking.Validator
import net.decentr.module_decentr_staking.presentation.extensions.format
import java.util.*

fun Staking.Validator.toDomain() = Validator(
    address = this.operatorAddress,
    name = this.description.moniker,
    jailed = this.jailed,
    status = when(this.status) {
        null,
        Staking.BondStatus.UNRECOGNIZED,
        Staking.BondStatus.BOND_STATUS_UNSPECIFIED -> Validator.Status.UNSPECIFIED
        Staking.BondStatus.BOND_STATUS_UNBONDED -> Validator.Status.UNBONDED
        Staking.BondStatus.BOND_STATUS_UNBONDING -> Validator.Status.UNBONDING
        Staking.BondStatus.BOND_STATUS_BONDED -> Validator.Status.BONDED
    },
    tokens = ((this.tokens.toLongOrNull() ?: DIVIDER_SMALL_LONG) / DIVIDER_SMALL_LONG).toString(),
    delegatorShares = this.delegatorShares,
    description = this.description.details,
    unbondingTime = Date(this.unbondingTime.seconds / DIVIDER_SEC),
    comission = Validator.Comission(
        rates = Validator.ComissionRate(
            rate = ((this.commission.commissionRates.rate.toLongOrNull() ?: DIVIDER_BIG) / DIVIDER_BIG).format(),
            maxRate = ((this.commission.commissionRates.maxRate.toLongOrNull() ?: DIVIDER_BIG) / DIVIDER_BIG).format(),
            maxChangeRate = ((this.commission.commissionRates.maxChangeRate.toLongOrNull() ?: DIVIDER_BIG) / DIVIDER_BIG).format()
        ),
        updatedTime = Date(this.commission.updateTime.seconds / DIVIDER_SEC)
    ),
    minSelfDelegation = ((this.minSelfDelegation).toLongOrNull() ?: 0L).format(),
    website = this.description.website
)