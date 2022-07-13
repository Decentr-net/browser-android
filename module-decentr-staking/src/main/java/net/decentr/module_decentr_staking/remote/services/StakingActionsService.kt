package net.decentr.module_decentr_staking.remote.services

import cosmos.staking.v1beta1.MsgGrpcKt
import cosmos.staking.v1beta1.Tx
import io.grpc.Channel
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import net.decentr.module_decentr_domain.errors.DecException
import net.decentr.module_decentr_domain.errors.ERROR_CODE_GRPC_CHANNEL_CLOSING
import net.decentr.module_decentr_staking.remote.utils.GrpcLoggingInterceptorKt
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StakingActionsService @Inject constructor() {

    private lateinit var channel: Channel

    fun init(host: String, port: Int) {
        if (this::channel.isInitialized) (channel as? ManagedChannel)?.shutdownNow()

        val channel = ManagedChannelBuilder
            .forAddress(host, port)
            .intercept(GrpcLoggingInterceptorKt())
            .keepAliveTime(10, TimeUnit.SECONDS)
            .keepAliveTimeout(5, TimeUnit.SECONDS)
            .executor(Dispatchers.IO.asExecutor())
            .usePlaintext()
            .build()

        this.channel = channel
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

    suspend fun delegate(request: Tx.MsgDelegate): Tx.MsgDelegateResponse {
        return MsgGrpcKt.MsgCoroutineStub(channel).delegate(request)
    }

    suspend fun undelegate(request: Tx.MsgUndelegate): Tx.MsgUndelegateResponse {
        return MsgGrpcKt.MsgCoroutineStub(channel).undelegate(request)
    }

    suspend fun beginRedelegate(request: Tx.MsgBeginRedelegate): Tx.MsgBeginRedelegateResponse {
        return MsgGrpcKt.MsgCoroutineStub(channel).beginRedelegate(request)
    }

}