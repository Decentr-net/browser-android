package net.decentr.module_decentr_staking.data.datasource

interface TxDataSource {
    suspend fun simulateRewardsWithdraw(privateKey: String, publicKey: String, validatorAddresses: ArrayList<String?>, delegatorAddress: String): Pair<Double, Double>
    suspend fun rewardsWithdraw(privateKey: String, publicKey: String, validatorAddresses: ArrayList<String?>, delegatorAddress: String, fee: String): String

    suspend fun simulateRedelegate(privateKey: String, publicKey: String, accountAddress: String, fromValidatorAddress: String, toValidatorAddress: String, redelegateAmount: String): Pair<Double, Double>
    suspend fun redelegate(privateKey: String, publicKey: String, accountAddress: String, fromValidatorAddress: String, toValidatorAddress: String, redelegateAmount: String, fee: String): String

    suspend fun simulateDelegate(privateKey: String, publicKey: String, accountAddress: String, toValidatorAddress: String, delegateAmount: String): Pair<Double, Double>
    suspend fun delegate(privateKey: String, publicKey: String, accountAddress: String, toValidatorAddress: String, delegateAmount: String, fee: String): String

    suspend fun simulateUndelegate(privateKey: String, publicKey: String, accountAddress: String, fromValidator: String, delegateAmount: String): Pair<Double, Double>
    suspend fun undelegate(privateKey: String, publicKey: String, accountAddress: String, fromValidator: String, delegateAmount: String, fee: String): String

    fun closeChannel()
}