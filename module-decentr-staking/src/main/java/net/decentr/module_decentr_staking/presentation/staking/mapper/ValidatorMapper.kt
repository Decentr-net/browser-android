package net.decentr.module_decentr_staking.presentation.staking.mapper

import net.decentr.module_decentr_domain.models.staking.Validator
import net.decentr.module_decentr_staking.presentation.extensions.format
import net.decentr.module_decentr_staking.presentation.extensions.removeEndingZeros
import net.decentr.module_decentr_staking.presentation.staking.viewstates.ValidatorViewState
import java.text.DecimalFormat

fun Validator.toViewState(boundedTokens: Long? = null, delegatorDelegations: List<Pair<String, String>>? = null) =
    ValidatorViewState(
        address = this.address,
        name = this.name,
        isJailed = this.jailed,
        votingPower = if (!this.tokens.isNullOrEmpty() && boundedTokens != null) {
            val formattedTokens = (this.tokens?.toLongOrNull() ?: 0L).format()

            val percent = (this.tokens!!.toDouble() / boundedTokens.toDouble()) * 100
            val decimalFormat = DecimalFormat("0.00")
            val formattedPercent = decimalFormat.format(percent.toBigDecimal())

            formattedTokens.plus(" (${formattedPercent.replace(',', '.').removeEndingZeros().trimEnd('.')}%)")
        } else {
         ""
        },
        votingPowerPercent = this.delegatorShares,
        status = when (this.status) {
            Validator.Status.UNSPECIFIED -> ValidatorViewState.Status.UNSPECIFIED
            Validator.Status.UNBONDED -> ValidatorViewState.Status.UNBONDED
            Validator.Status.UNBONDING -> ValidatorViewState.Status.UNBONDING
            Validator.Status.BONDED -> ValidatorViewState.Status.BONDED
        },
        commission = this.comission.rates.rate,
        delegatedAmount =
            delegatorDelegations?.firstOrNull {
                val (address, tokens) = it
                address == this.address
            }?.second.let {
                val value = it?.toDouble()
                val decimalFormat = DecimalFormat("0.000")
                value?.let { decimalFormat.format(it).replace(',', '.').removeEndingZeros().trimEnd('.') }
            } ?: "0",
        website = website,
        description = description
    )

