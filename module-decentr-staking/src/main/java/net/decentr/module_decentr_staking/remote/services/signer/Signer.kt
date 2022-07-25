package net.decentr.module_decentr_staking.remote.services.signer

import com.google.protobuf.ByteString
import cosmos.auth.v1beta1.Auth
import cosmos.auth.v1beta1.AuthQuery
import cosmos.base.v1beta1.CoinOuterClass.Coin
import cosmos.crypto.secp256k1.Keys
import cosmos.distribution.v1beta1.DistributionMsg
import cosmos.staking.v1beta1.Tx.*
import cosmos.tx.signing.v1beta1.Signing
import cosmos.tx.v1beta1.ServiceOuterClass
import cosmos.tx.v1beta1.ServiceOuterClass.BroadcastTxRequest
import cosmos.tx.v1beta1.TxOuterClass.*
import net.decentr.module_decentr_common.data.utils.Sha256
import net.decentr.module_decentr_common.data.utils.etheriumkit.*
import net.decentr.module_decentr_staking.presentation.extensions.removeEndingZeros
import java.math.BigDecimal
import java.security.MessageDigest

object Signer {

    const val TOKEN_DEC = "udec"
    const val gasPrice = 0.025
    const val gasAdjuster = 1.3
    const val memoSimulation = "With simulate"
    const val memoTarget = ""
    const val decentrChainId = "mainnet-3"
    const val decentrTestChainId = "testnet-1.5.0"

    fun getClaimRewardsMsg(
        toValAddresses: ArrayList<String?>,
        delegatorAddress: String
    ): ArrayList<com.google.protobuf.AnyOuterClass.Any> {
        val msgAnys = ArrayList<com.google.protobuf.AnyOuterClass.Any>()
        for (valAddr in toValAddresses) {
            val msgClaimReward = DistributionMsg.MsgWithdrawDelegatorReward.newBuilder()
                .setDelegatorAddress(delegatorAddress)
                .setValidatorAddress(valAddr).build()
            val msgClaimRewardAny: com.google.protobuf.AnyOuterClass.Any = com.google.protobuf.AnyOuterClass.Any.newBuilder()
                .setTypeUrl("/cosmos.distribution.v1beta1.MsgWithdrawDelegatorReward")
                .setValue(msgClaimReward.toByteString()).build()
            msgAnys.add(msgClaimRewardAny)
        }
        return msgAnys
    }

    fun getReDelegateMsg(
        delegatorAddress: String,
        fromValAddress: String?,
        toValAddress: String?,
        amounts: Coin
    ): ArrayList<com.google.protobuf.AnyOuterClass.Any> {
        val msgAnys = ArrayList<com.google.protobuf.AnyOuterClass.Any>()
        val toReDelegateCoin =
            Coin.newBuilder().setAmount(amounts.amount).setDenom(amounts.denom).build()
        val msgReDelegate = MsgBeginRedelegate.newBuilder()
            .setDelegatorAddress(delegatorAddress)
            .setValidatorSrcAddress(fromValAddress).setValidatorDstAddress(toValAddress)
            .setAmount(toReDelegateCoin).build()
        msgAnys.add(
            com.google.protobuf.AnyOuterClass.Any.newBuilder().setTypeUrl("/cosmos.staking.v1beta1.MsgBeginRedelegate")
                .setValue(msgReDelegate.toByteString()).build()
        )
        return msgAnys
    }

    fun getUnDelegateMsg(
        delegatorAddress: String,
        fromValidator: String?,
        amounts: Coin
    ): ArrayList<com.google.protobuf.AnyOuterClass.Any> {
        val msgAnys = ArrayList<com.google.protobuf.AnyOuterClass.Any>()
        val toDelegateCoin =
            Coin.newBuilder().setAmount(amounts.amount).setDenom(amounts.denom).build()
        val msgUnDelegate = MsgUndelegate.newBuilder()
            .setDelegatorAddress(delegatorAddress)
            .setValidatorAddress(fromValidator).setAmount(toDelegateCoin).build()
        msgAnys.add(
            com.google.protobuf.AnyOuterClass.Any.newBuilder().setTypeUrl("/cosmos.staking.v1beta1.MsgUndelegate")
                .setValue(msgUnDelegate.toByteString()).build()
        )
        return msgAnys
    }

    fun getDelegateMsg(
        delegatorAddress: String,
        toValAddress: String?,
        amounts: Coin
    ): ArrayList<com.google.protobuf.AnyOuterClass.Any> {
        val msgAnys = ArrayList<com.google.protobuf.AnyOuterClass.Any>()
        val toDelegateCoin =
            Coin.newBuilder().setAmount(amounts.amount).setDenom(amounts.denom).build()
        val msgDelegate = MsgDelegate.newBuilder()
            .setDelegatorAddress(delegatorAddress)
            .setValidatorAddress(toValAddress).setAmount(toDelegateCoin).build()
        msgAnys.add(
            com.google.protobuf.AnyOuterClass.Any.newBuilder().setTypeUrl("/cosmos.staking.v1beta1.MsgDelegate")
                .setValue(msgDelegate.toByteString()).build()
        )
        return msgAnys
    }

    fun getSignTx(
        auth: AuthQuery.QueryAccountResponse?,
        msgAnys: ArrayList<com.google.protobuf.AnyOuterClass.Any>,
        fee: Fee,
        memo: String?,
        pKey: ECKey,
        chainId: String?,
        publicKey: String
    ): BroadcastTxRequest? {
        val txBody: TxBody = getGrpcTxBodys(msgAnys, memo) ?: throw IllegalStateException("cannot be tx null")
        val signerInfo: SignerInfo? = getGrpcSignerInfo(auth, publicKey)
        val authInfo: AuthInfo = getGrpcAuthInfo(signerInfo, fee) ?: throw IllegalStateException("cannot be auth info null")
        val rawTx: TxRaw = getGrpcRawTx(auth, txBody, authInfo, pKey, chainId) ?: throw IllegalStateException("cannot be tx null")
        return BroadcastTxRequest.newBuilder()
            .setModeValue(ServiceOuterClass.BroadcastMode.BROADCAST_MODE_SYNC.number)
            .setTxBytes(rawTx.toByteString()).build()
    }

    fun getSignSimulTx(
        auth: AuthQuery.QueryAccountResponse?,
        msgAnys: ArrayList<com.google.protobuf.AnyOuterClass.Any>,
        fee: Fee,
        memo: String?,
        pKey: ECKey,
        chainId: String?,
        publicKey: String
    ): ServiceOuterClass.SimulateRequest? {
        val txBody: TxBody =
            getGrpcTxBodys(msgAnys, memo) ?: throw IllegalStateException("cannot be tx null")
        val signerInfo: SignerInfo? = getGrpcSignerInfo(auth, publicKey)
        val authInfo: AuthInfo = getGrpcAuthInfo(signerInfo, fee) ?: throw IllegalStateException("cannot be auth info null")
        val simulateTx: Tx? = getGrpcSimulTx(auth, txBody, authInfo, pKey, chainId)
        return ServiceOuterClass.SimulateRequest.newBuilder().setTxBytes(simulateTx?.toByteString())
            .build()
    }

    private fun getGrpcTxBodys(
        msgsAny: ArrayList<com.google.protobuf.AnyOuterClass.Any>,
        memo: String?
    ): TxBody? {
        val builder = TxBody.newBuilder()
        for (msg in msgsAny) {
            builder.addMessages(msg)
        }
        return builder.setMemo(memo).build()
    }

    private fun getGrpcSignerInfo(
        auth: AuthQuery.QueryAccountResponse?,
        publicKey: String
    ): SignerInfo? {
        val pubKey: com.google.protobuf.AnyOuterClass.Any? = generateGrpcPubKeyFromPriv(
            auth,
            publicKey
        )
        val singleMode =
            ModeInfo.Single.newBuilder().setMode(Signing.SignMode.SIGN_MODE_DIRECT)
                .build()
        val modeInfo = ModeInfo.newBuilder().setSingle(singleMode).build()
        val account = Auth.BaseAccount.parseFrom(auth?.account?.value)

        return SignerInfo.newBuilder().setPublicKey(pubKey).setModeInfo(modeInfo)
            .setSequence(account.sequence).build()
    }

    private fun getGrpcAuthInfo(
        signerInfo: SignerInfo?,
        fee: Fee
    ): AuthInfo? {
        val feeCoin =
            Coin.newBuilder().setAmount(fee.amountList[0].amount)
                .setDenom(fee.amountList[0].denom)
                .build()
        val txFee =
            Fee.newBuilder().addAmount(feeCoin).setGasLimit(100000000000L).build()
        return AuthInfo.newBuilder().setFee(txFee).addSignerInfos(signerInfo).build()
    }

    private fun getGrpcRawTx(
        auth: AuthQuery.QueryAccountResponse?,
        txBody: TxBody,
        authInfo: AuthInfo,
        pKey: ECKey,
        chainId: String?
    ): TxRaw? {
        val account = Auth.BaseAccount.parseFrom(auth?.account?.value)

        val signDoc = SignDoc.newBuilder().setBodyBytes(txBody.toByteString())
            .setAuthInfoBytes(authInfo.toByteString()).setChainId(chainId)
            .setAccountNumber(account.accountNumber)
            .build()

        val digest: MessageDigest = Sha256.getSha256Digest()
        val toSignHash = digest.digest(signDoc.toByteArray())
        val sigbyte: ByteArray = CryptoUtils.ellipticSign(
            toSignHash,
            pKey.privateKey)

        return TxRaw.newBuilder().setBodyBytes(txBody.toByteString())
            .setAuthInfoBytes(authInfo.toByteString()).addSignatures(
                ByteString.copyFrom(sigbyte)
            ).build()
    }

    private fun getGrpcSimulTx(
        auth: AuthQuery.QueryAccountResponse?,
        txBody: TxBody,
        authInfo: AuthInfo,
        pKey: ECKey,
        chainId: String?
    ): Tx? {
        val account = Auth.BaseAccount.parseFrom(auth?.account?.value)

        val signDoc = SignDoc.newBuilder().setBodyBytes(txBody.toByteString())
            .setAuthInfoBytes(authInfo.toByteString()).setChainId(chainId)
            .setAccountNumber(account.accountNumber)
            .build()

        val digest: MessageDigest = Sha256.getSha256Digest()
        val toSignHash = digest.digest(signDoc.toByteArray())
        val sigbyte: ByteArray = CryptoUtils.ellipticSign(
            toSignHash,
            pKey.privateKey)

        return Tx.newBuilder().setAuthInfo(authInfo).setBody(txBody)
            .addSignatures(ByteString.copyFrom(sigbyte)).build()
    }

    // For gRpc Keys
    private fun generateGrpcPubKeyFromPriv(
        auth: AuthQuery.QueryAccountResponse?,
        publicKey: String
    ): com.google.protobuf.AnyOuterClass.Any? {
        val pubKey =
            Keys.PubKey.newBuilder()
                .setKey(ByteString.copyFrom(publicKey.hexStringToByteArray())).build()
        return com.google.protobuf.AnyOuterClass.Any.newBuilder().setTypeUrl("/cosmos.crypto.secp256k1.PubKey")
            .setValue(pubKey.toByteString()).build()
//        }
    }

    fun adjustGas(gasPair: Pair<Long, Long>): Pair<Double, Double> {
        val (gasUsed, gasWanted) = gasPair
        return (gasUsed * gasPrice * gasAdjuster) to (gasWanted * gasPrice * gasAdjuster)
    }

    fun reAdjustGas(fee: Double): String {
        return (fee).toInt().toString()
    }

    fun isValidateDelegateAmount(amount: String): Coin {
        try {
            val amountTemp = BigDecimal(amount.trim { it <= ' ' })
            if (amountTemp <= BigDecimal.ZERO) throw IllegalStateException("Amount cannot be 0")
            return Coin.newBuilder().setAmount(amountTemp.setScale(0).toPlainString()).setDenom(TOKEN_DEC).build()
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }
    }
}