package net.decentr.module_decentr_domain.repository

interface TxRepository {
    suspend fun simulateRewardsWithdraw(validatorAddresses: ArrayList<String?>, delegatorAddress: String): Pair<Double, Double>
    suspend fun rewardsWithdraw(validatorAddresses: ArrayList<String?>, delegatorAddress: String, fee: String): String

    suspend fun simulateRedelegate(accountAddress: String, fromValidatorAddress: String, toValidatorAddress: String, redelegateAmount: String): Pair<Double, Double>
    suspend fun redelegate(accountAddress: String, fromValidatorAddress: String, toValidatorAddress: String, redelegateAmount: String, fee: String): String

    suspend fun simulateDelegate(accountAddress: String, toValidatorAddress: String, delegateAmount: String): Pair<Double, Double>
    suspend fun delegate(accountAddress: String, toValidatorAddress: String, delegateAmount: String, fee: String): String

    suspend fun simulateUndelegate(accountAddress: String, fromValidator: String, delegateAmount: String): Pair<Double, Double>
    suspend fun undelegate(accountAddress: String, fromValidator: String, delegateAmount: String, fee: String): String

    fun closeChannel()
}