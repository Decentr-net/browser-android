package net.decentr.module_decentr_staking.remote.datasource

import net.decentr.module_decentr_staking.data.datasource.TxDataSource
import net.decentr.module_decentr_staking.data.utils.configNetworkHosts
import net.decentr.module_decentr_staking.data.utils.configNetworkPort
import net.decentr.module_decentr_staking.remote.mapper.DIVIDER_SMALL
import net.decentr.module_decentr_staking.remote.services.TxService
import net.decentr.module_decentr_staking.remote.services.signer.Signer.decentrChainId
import net.decentr.module_decentr_staking.remote.services.signer.Signer.decentrTestChainId
import javax.inject.Inject

class TxDataSourceImpl @Inject constructor(private val txService: TxService): TxDataSource {

    init {
        val host = configNetworkHosts.random()
        val port = configNetworkPort
        val chainId = decentrChainId
        txService.init(host, port, chainId)
    }

    override suspend fun simulateRewardsWithdraw(privateKey: String, publicKey: String, validatorAddresses: ArrayList<String?>, delegatorAddress: String): Pair<Double, Double> {
        val (gasWanted, gasUsed) = txService.simulateRewardsWithdraw(privateKey, publicKey, validatorAddresses, delegatorAddress)
        return gasWanted.div(DIVIDER_SMALL.toDouble()) to gasUsed.div(DIVIDER_SMALL.toDouble())
    }

    override suspend fun rewardsWithdraw(
        privateKey: String,
        publicKey: String,
        validatorAddresses: ArrayList<String?>,
        delegatorAddress: String,
        fee: String
    ): String {
        return txService.rewardsWithdraw(privateKey, publicKey, validatorAddresses, delegatorAddress, fee.toDouble().times(DIVIDER_SMALL.toDouble()))
    }

    override suspend fun simulateRedelegate(
        privateKey: String,
        publicKey: String,
        accountAddress: String,
        fromValidatorAddress: String,
        toValidatorAddress: String,
        redelegateAmount: String
    ): Pair<Double, Double> {
        val (gasWanted, gasUsed) = txService.simulateRedelegate(privateKey, publicKey, accountAddress, fromValidatorAddress, toValidatorAddress, redelegateAmount.toDouble().times(DIVIDER_SMALL.toDouble()).toString())
        return gasWanted.div(DIVIDER_SMALL.toDouble()) to gasUsed.div(DIVIDER_SMALL.toDouble())
    }

    override suspend fun redelegate(
        privateKey: String,
        publicKey: String,
        accountAddress: String,
        fromValidatorAddress: String,
        toValidatorAddress: String,
        redelegateAmount: String,
        fee: String
    ): String {
        return txService.redelegate(privateKey, publicKey, accountAddress, fromValidatorAddress, toValidatorAddress, redelegateAmount.toDouble().times(DIVIDER_SMALL.toDouble()).toString(), fee.toDouble().times(DIVIDER_SMALL.toDouble()))
    }

    override suspend fun simulateDelegate(
        privateKey: String,
        publicKey: String,
        accountAddress: String,
        toValidatorAddress: String,
        delegateAmount: String
    ): Pair<Double, Double> {
        val (gasWanted, gasUsed) = txService.simulationDelegate(privateKey, publicKey, accountAddress, toValidatorAddress, delegateAmount.toDouble().times(DIVIDER_SMALL.toDouble()).toString())
        return gasWanted.div(DIVIDER_SMALL.toDouble()) to gasUsed.div(DIVIDER_SMALL.toDouble())
    }

    override suspend fun delegate(
        privateKey: String,
        publicKey: String,
        accountAddress: String,
        toValidatorAddress: String,
        delegateAmount: String,
        fee: String
    ): String {
        return txService.delegate(privateKey, publicKey, accountAddress, toValidatorAddress, delegateAmount.toDouble().times(DIVIDER_SMALL.toDouble()).toString(), fee.toDouble().times(DIVIDER_SMALL.toDouble()))
    }

    override suspend fun simulateUndelegate(
        privateKey: String,
        publicKey: String,
        accountAddress: String,
        fromValidator: String,
        delegateAmount: String
    ): Pair<Double, Double> {
        val (gasWanted, gasUsed) = txService.simulationUndelegate(privateKey, publicKey, accountAddress, fromValidator, delegateAmount.toDouble().times(DIVIDER_SMALL.toDouble()).toString())
        return gasWanted.div(DIVIDER_SMALL.toDouble()) to gasUsed.div(DIVIDER_SMALL.toDouble())
    }

    override suspend fun undelegate(
        privateKey: String,
        publicKey: String,
        accountAddress: String,
        fromValidator: String,
        delegateAmount: String,
        fee: String
    ): String {
        return txService.undelegate(privateKey, publicKey, accountAddress, fromValidator, delegateAmount.toDouble().times(DIVIDER_SMALL.toDouble()).toString(), fee.toDouble().times(DIVIDER_SMALL.toDouble()))
    }

    override fun closeChannel() {
        txService.closeChannel()
    }
}