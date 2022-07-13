package net.decentr.module_decentr_staking.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.decentr.module_decentr_common.data.provider.KeysProvider
import net.decentr.module_decentr_domain.repository.TxRepository
import net.decentr.module_decentr_staking.data.datasource.TxDataSource
import javax.inject.Inject

class TxRepositoryImpl @Inject constructor(
    private val remoteDataSource: TxDataSource,
    private val keysProvider: KeysProvider,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TxRepository {

    override suspend fun simulateRewardsWithdraw(
        validatorAddresses: ArrayList<String?>,
        delegatorAddress: String
    ): Pair<Double, Double> {
        return withContext(ioDispatcher) {
            val privateKey =
                keysProvider.getPrivKey() ?: throw IllegalStateException("no private key")
            val publicKey =
                keysProvider.getPubKey() ?: throw java.lang.IllegalStateException("no public key")
            remoteDataSource.simulateRewardsWithdraw(
                privateKey,
                publicKey,
                validatorAddresses,
                delegatorAddress
            )
        }
    }

    override suspend fun rewardsWithdraw(
        validatorAddresses: ArrayList<String?>, delegatorAddress: String, fee: String
    ): String {
        return withContext(ioDispatcher) {
            val privateKey =
                keysProvider.getPrivKey() ?: throw IllegalStateException("no private key")
            val publicKey =
                keysProvider.getPubKey() ?: throw java.lang.IllegalStateException("no public key")
            remoteDataSource.rewardsWithdraw(
                privateKey,
                publicKey,
                validatorAddresses,
                delegatorAddress,
                fee
            )
        }
    }

    override suspend fun simulateRedelegate(
        accountAddress: String,
        fromValidatorAddress: String,
        toValidatorAddress: String,
        redelegateAmount: String
    ): Pair<Double, Double> {
        return withContext(ioDispatcher) {
            val privateKey =
                keysProvider.getPrivKey() ?: throw IllegalStateException("no private key")
            val publicKey =
                keysProvider.getPubKey() ?: throw java.lang.IllegalStateException("no public key")
            remoteDataSource.simulateRedelegate(
                privateKey,
                publicKey,
                accountAddress,
                fromValidatorAddress,
                toValidatorAddress,
                redelegateAmount,
            )
        }
    }

    override suspend fun redelegate(
        accountAddress: String,
        fromValidatorAddress: String,
        toValidatorAddress: String,
        redelegateAmount: String,
        fee: String
    ): String {
        return withContext(ioDispatcher) {
            val privateKey =
                keysProvider.getPrivKey() ?: throw IllegalStateException("no private key")
            val publicKey =
                keysProvider.getPubKey() ?: throw java.lang.IllegalStateException("no public key")
            remoteDataSource.redelegate(
                privateKey,
                publicKey,
                accountAddress,
                fromValidatorAddress,
                toValidatorAddress,
                redelegateAmount,
                fee
            )
        }
    }

    override suspend fun simulateDelegate(
        accountAddress: String,
        toValidatorAddress: String,
        delegateAmount: String
    ): Pair<Double, Double> {
        return withContext(ioDispatcher) {
            val privateKey =
                keysProvider.getPrivKey() ?: throw IllegalStateException("no private key")
            val publicKey =
                keysProvider.getPubKey() ?: throw java.lang.IllegalStateException("no public key")
            remoteDataSource.simulateDelegate(
                privateKey,
                publicKey,
                accountAddress,
                toValidatorAddress,
                delegateAmount
            )
        }
    }

    override suspend fun delegate(
        accountAddress: String,
        toValidatorAddress: String,
        delegateAmount: String,
        fee: String
    ): String {
        return withContext(ioDispatcher) {
            val privateKey =
                keysProvider.getPrivKey() ?: throw IllegalStateException("no private key")
            val publicKey =
                keysProvider.getPubKey() ?: throw java.lang.IllegalStateException("no public key")
            remoteDataSource.delegate(
                privateKey,
                publicKey,
                accountAddress,
                toValidatorAddress,
                delegateAmount,
                fee
            )
        }
    }

    override suspend fun simulateUndelegate(
        accountAddress: String,
        fromValidator: String,
        delegateAmount: String
    ): Pair<Double, Double> {
        return withContext(ioDispatcher) {
            val privateKey =
                keysProvider.getPrivKey() ?: throw IllegalStateException("no private key")
            val publicKey =
                keysProvider.getPubKey() ?: throw java.lang.IllegalStateException("no public key")
            remoteDataSource.simulateUndelegate(
                privateKey,
                publicKey,
                accountAddress,
                fromValidator,
                delegateAmount
            )
        }
    }

    override suspend fun undelegate(
        accountAddress: String,
        fromValidator: String,
        delegateAmount: String,
        fee: String
    ): String {
        return withContext(ioDispatcher) {
            val privateKey =
                keysProvider.getPrivKey() ?: throw IllegalStateException("no private key")
            val publicKey =
                keysProvider.getPubKey() ?: throw java.lang.IllegalStateException("no public key")
            remoteDataSource.undelegate(
                privateKey,
                publicKey,
                accountAddress,
                fromValidator,
                delegateAmount,
                fee
            )
        }
    }

    override fun closeChannel() {
        remoteDataSource.closeChannel()
    }
}