package net.decentr.module_decentr_staking.remote.services

import cosmos.auth.v1beta1.AuthQuery
import cosmos.auth.v1beta1.QueryGrpcKt
import cosmos.base.v1beta1.CoinOuterClass.Coin
import cosmos.tx.v1beta1.ServiceGrpcKt
import cosmos.tx.v1beta1.ServiceOuterClass
import cosmos.tx.v1beta1.ServiceOuterClass.BroadcastTxRequest
import cosmos.tx.v1beta1.ServiceOuterClass.SimulateRequest
import cosmos.tx.v1beta1.TxOuterClass.Fee
import io.grpc.Channel
import io.grpc.ManagedChannel
import io.grpc.okhttp.OkHttpChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import net.decentr.module_decentr_common.data.utils.etheriumkit.CryptoUtils
import net.decentr.module_decentr_common.data.utils.etheriumkit.hexStringWithoutStripToBigIntegerOrNull
import net.decentr.module_decentr_domain.errors.DecException
import net.decentr.module_decentr_domain.errors.ERROR_CODE_GRPC_CHANNEL_CLOSING
import net.decentr.module_decentr_staking.remote.services.signer.Signer
import net.decentr.module_decentr_staking.remote.services.signer.Signer.TOKEN_DEC
import net.decentr.module_decentr_staking.remote.services.signer.Signer.isValidateDelegateAmount
import net.decentr.module_decentr_staking.remote.services.signer.Signer.memoSimulation
import net.decentr.module_decentr_staking.remote.services.signer.Signer.memoTarget
import net.decentr.module_decentr_staking.remote.utils.GrpcLoggingInterceptorKt
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TxService @Inject constructor() {

    private lateinit var channel: Channel
    private lateinit var chainId: String

    fun init(host: String, port: Int, chainId: String) {
        if (this::channel.isInitialized) (channel as? ManagedChannel)?.shutdownNow()

        val channel = OkHttpChannelBuilder
            .forAddress(host, port)
            .intercept(GrpcLoggingInterceptorKt())
            .keepAliveTime(20, TimeUnit.SECONDS)
            .keepAliveTimeout(5, TimeUnit.SECONDS)
            .executor(Dispatchers.IO.asExecutor())
            .usePlaintext()
            .build()

        this.channel = channel
        this.chainId = chainId
    }

    fun closeChannel() {
        if (this::channel.isInitialized) {
            (channel as? ManagedChannel)?.shutdown()
            try {
                (channel as? ManagedChannel)?.awaitTermination(5, TimeUnit.SECONDS)
            } catch (e: DecException) {
                throw DecException(code = ERROR_CODE_GRPC_CHANNEL_CLOSING)
            }
        }
    }

    suspend fun simulateRewardsWithdraw(privateKey: String, publicKey: String, validatorAddresses: ArrayList<String?>, delegatorAddress: String): Pair<Double, Double> {
        val ecKey = CryptoUtils.ecKeyFromPrivate(
            privateKey.hexStringWithoutStripToBigIntegerOrNull()
                ?: throw IllegalStateException("unable to parse private key")
        )
        val authResponse = getAuthResponse(delegatorAddress)
        val fee = Fee.newBuilder().addAmount(Coin.newBuilder().setAmount("0").setDenom(TOKEN_DEC).build()).build()
        val simulationRequest = Signer.getSignSimulTx(
            authResponse,
            Signer.getClaimRewardsMsg(validatorAddresses, delegatorAddress),
            fee,
            memoSimulation,
            ecKey,
            chainId,
            publicKey
        )
        val response = simulateTx(simulationRequest?.txBytes)

        return Signer.adjustGas(response.gasInfo.gasUsed to response.gasInfo.gasWanted)
    }

    suspend fun rewardsWithdraw(privateKey: String, publicKey: String, validatorAddresses: ArrayList<String?>, delegatorAddress: String, fee: Double): String {
        val ecKey = CryptoUtils.ecKeyFromPrivate(
            privateKey.hexStringWithoutStripToBigIntegerOrNull()
                ?: throw IllegalStateException("unable to parse private key")
        )
        val authResponse = getAuthResponse(delegatorAddress)
        val feeObj = Fee.newBuilder().addAmount(Coin.newBuilder().setAmount(Signer.reAdjustGas(fee)).setDenom(TOKEN_DEC).build()).build()

        val broadcastTxRequest: BroadcastTxRequest = Signer.getSignTx(
            authResponse,
            Signer.getClaimRewardsMsg(validatorAddresses, delegatorAddress),
            feeObj,
            memoTarget,
            ecKey,
            chainId,
            publicKey
        ) ?: throw IllegalStateException("broadcast cannot be null")
        val response = broadcastTx(broadcastTxRequest)

        return response.txResponse.txhash
    }

    suspend fun simulateRedelegate(privateKey: String, publicKey: String, accountAddress: String, fromValidatorAddress: String, toValidatorAddress: String, redelegateAmount: String): Pair<Double, Double> {
        val ecKey = CryptoUtils.ecKeyFromPrivate(
            privateKey.hexStringWithoutStripToBigIntegerOrNull()
                ?: throw IllegalStateException("unable to parse private key")
        )
        val authResponse = getAuthResponse(accountAddress)
        val fee = Fee.newBuilder().addAmount(Coin.newBuilder().setAmount("0").setDenom(TOKEN_DEC).build()).build()

        try {
            val simulationRequest = Signer.getSignSimulTx(
                authResponse,
                Signer.getReDelegateMsg(accountAddress, fromValidatorAddress, toValidatorAddress, isValidateDelegateAmount(redelegateAmount)),
                fee,
                memoSimulation,
                ecKey,
                chainId,
                publicKey
            )
            val response = simulateTx(simulationRequest?.txBytes)

            return Signer.adjustGas(response.gasInfo.gasUsed to response.gasInfo.gasWanted)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun redelegate(privateKey: String, publicKey: String, accountAddress: String, fromValidatorAddress: String, toValidatorAddress: String, redelegateAmount: String, fee: Double): String {
        val ecKey = CryptoUtils.ecKeyFromPrivate(
            privateKey.hexStringWithoutStripToBigIntegerOrNull()
                ?: throw IllegalStateException("unable to parse private key")
        )
        val authResponse = getAuthResponse(accountAddress)
        val feeObj = Fee.newBuilder().addAmount(Coin.newBuilder().setAmount(Signer.reAdjustGas(fee)).setDenom(TOKEN_DEC).build()).build()

        try {
            val broadcastTxRequest: BroadcastTxRequest = Signer.getSignTx(
                authResponse,
                Signer.getReDelegateMsg(accountAddress, fromValidatorAddress, toValidatorAddress, isValidateDelegateAmount(redelegateAmount)),
                feeObj,
                memoTarget,
                ecKey,
                chainId,
                publicKey
            ) ?: throw IllegalStateException("broadcast cannot be null")
            val response = broadcastTx(broadcastTxRequest)

            return response.txResponse.txhash
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun simulationDelegate(privateKey: String, publicKey: String, accountAddress: String, toValidatorAddress: String, delegateAmount: String): Pair<Double, Double> {
        val ecKey = CryptoUtils.ecKeyFromPrivate(
            privateKey.hexStringWithoutStripToBigIntegerOrNull()
                ?: throw IllegalStateException("unable to parse private key")
        )
        val authResponse = getAuthResponse(accountAddress)
        val fee = Fee.newBuilder().addAmount(Coin.newBuilder().setAmount("0").setDenom(TOKEN_DEC).build()).build()

        try {
            val simulationRequest = Signer.getSignSimulTx(
                authResponse,
                Signer.getDelegateMsg(accountAddress, toValidatorAddress, isValidateDelegateAmount(delegateAmount)),
                fee,
                memoSimulation,
                ecKey,
                chainId,
                publicKey
            )
            val response = simulateTx(simulationRequest?.txBytes)

            return Signer.adjustGas(response.gasInfo.gasUsed to response.gasInfo.gasWanted)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun delegate(privateKey: String, publicKey: String, accountAddress: String, toValidatorAddress: String, delegateAmount: String, fee: Double): String {
        val ecKey = CryptoUtils.ecKeyFromPrivate(
            privateKey.hexStringWithoutStripToBigIntegerOrNull()
                ?: throw IllegalStateException("unable to parse private key")
        )
        val authResponse = getAuthResponse(accountAddress)
        val feeObj = Fee.newBuilder().addAmount(Coin.newBuilder().setAmount(Signer.reAdjustGas(fee)).setDenom(TOKEN_DEC).build()).build()

        try {
            val broadcastTxRequest: BroadcastTxRequest = Signer.getSignTx(
                authResponse,
                Signer.getDelegateMsg(accountAddress, toValidatorAddress, isValidateDelegateAmount(delegateAmount)),
                feeObj,
                memoTarget,
                ecKey,
                chainId,
                publicKey
            ) ?: throw IllegalStateException("broadcast cannot be null")
            val response = broadcastTx(broadcastTxRequest)

            return response.txResponse.txhash
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun simulationUndelegate(privateKey: String, publicKey: String, accountAddress: String, fromValidator: String, delegateAmount: String): Pair<Double, Double> {
        val ecKey = CryptoUtils.ecKeyFromPrivate(
            privateKey.hexStringWithoutStripToBigIntegerOrNull()
                ?: throw IllegalStateException("unable to parse private key")
        )
        val authResponse = getAuthResponse(accountAddress)
        val fee = Fee.newBuilder().addAmount(Coin.newBuilder().setAmount("0").setDenom(TOKEN_DEC).build()).build()

        try {
            val simulationRequest = Signer.getSignSimulTx(
                authResponse,
                Signer.getUnDelegateMsg(accountAddress, fromValidator, isValidateDelegateAmount(delegateAmount)),
                fee,
                memoSimulation,
                ecKey,
                chainId,
                publicKey
            )
            val response = simulateTx(simulationRequest?.txBytes)

            return Signer.adjustGas(response.gasInfo.gasUsed to response.gasInfo.gasWanted)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun undelegate(privateKey: String, publicKey: String, accountAddress: String, fromValidator: String, delegateAmount: String, fee: Double): String {
        val ecKey = CryptoUtils.ecKeyFromPrivate(
            privateKey.hexStringWithoutStripToBigIntegerOrNull()
                ?: throw IllegalStateException("unable to parse private key")
        )
        val authResponse = getAuthResponse(accountAddress)
        val feeObj = Fee.newBuilder().addAmount(Coin.newBuilder().setAmount(Signer.reAdjustGas(fee)).setDenom(TOKEN_DEC).build()).build()

        try {
            val broadcastTxRequest: BroadcastTxRequest = Signer.getSignTx(
                authResponse,
                Signer.getUnDelegateMsg(accountAddress, fromValidator, isValidateDelegateAmount(delegateAmount)),
                feeObj,
                memoTarget,
                ecKey,
                chainId,
                publicKey
            ) ?: throw IllegalStateException("broadcast cannot be null")
            val response = broadcastTx(broadcastTxRequest)

            return response.txResponse.txhash
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getAuthResponse(address: String): AuthQuery.QueryAccountResponse {
        val request: AuthQuery.QueryAccountRequest =
            AuthQuery.QueryAccountRequest.newBuilder().setAddress(address).build()
        return QueryGrpcKt.QueryCoroutineStub(channel).account(request)
    }

    private suspend fun simulateTx(txBytes: com.google.protobuf.ByteString?): ServiceOuterClass.SimulateResponse {
        try {
            val txService = ServiceGrpcKt.ServiceCoroutineStub(channel)
            val simulateTxRequest = SimulateRequest.newBuilder().setTxBytes(txBytes).build()
            return txService.simulate(simulateTxRequest)
        } catch (e: Exception) {
            throw DecException(exception = e)
        }
    }

    private suspend fun broadcastTx(request: BroadcastTxRequest): ServiceOuterClass.BroadcastTxResponse {
        try {
            val txService = ServiceGrpcKt.ServiceCoroutineStub(channel)
            return txService.broadcastTx(request)
        } catch (e: Exception) {
            throw DecException(exception = e)
        }
    }
}