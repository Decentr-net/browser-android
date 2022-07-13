package net.decentr.module_decentr_staking.remote.services

import cosmos.staking.v1beta1.QueryGrpcKt
import cosmos.staking.v1beta1.QueryOuterClass
import io.grpc.Channel
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.internal.GrpcUtil.CONTENT_TYPE_GRPC
import io.grpc.internal.GrpcUtil.CONTENT_TYPE_KEY
import io.grpc.okhttp.OkHttpChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import net.decentr.module_decentr_domain.errors.DecException
import net.decentr.module_decentr_domain.errors.ERROR_CODE_GRPC_CHANNEL_CLOSING
import net.decentr.module_decentr_staking.remote.utils.GrpcLoggingInterceptor
import net.decentr.module_decentr_staking.remote.utils.GrpcLoggingInterceptorKt
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StakingService @Inject constructor() {

    private lateinit var channel: Channel

    fun init(host: String, port: Int) {
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

    suspend fun pool(request: QueryOuterClass.QueryPoolRequest): QueryOuterClass.QueryPoolResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).pool(request)
    }

    suspend fun validators(request: QueryOuterClass.QueryValidatorsRequest): QueryOuterClass.QueryValidatorsResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).validators(request)
    }

    suspend fun validator(request: QueryOuterClass.QueryValidatorRequest): QueryOuterClass.QueryValidatorResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).validator(request)
    }

    suspend fun validatorDelegations(request: QueryOuterClass.QueryValidatorDelegationsRequest): QueryOuterClass.QueryValidatorDelegationsResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).validatorDelegations(request)
    }

    suspend fun delegatorDelegations(request: QueryOuterClass.QueryDelegatorDelegationsRequest): QueryOuterClass.QueryDelegatorDelegationsResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).delegatorDelegations(request)
    }

    suspend fun delegation(request: QueryOuterClass.QueryDelegationRequest): QueryOuterClass.QueryDelegationResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).delegation(request)
    }

    suspend fun delegatorUnbondingDelegations(request: QueryOuterClass.QueryDelegatorUnbondingDelegationsRequest): QueryOuterClass.QueryDelegatorUnbondingDelegationsResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).delegatorUnbondingDelegations(request)
    }

    suspend fun unbondingDelegation(request: QueryOuterClass.QueryUnbondingDelegationRequest): QueryOuterClass.QueryUnbondingDelegationResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).unbondingDelegation(request)
    }

    suspend fun validatorUnbondingDelegations(request: QueryOuterClass.QueryValidatorUnbondingDelegationsRequest): QueryOuterClass.QueryValidatorUnbondingDelegationsResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).validatorUnbondingDelegations(request)
    }

    suspend fun redelegations(request: QueryOuterClass.QueryRedelegationsRequest): QueryOuterClass.QueryRedelegationsResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).redelegations(request)
    }

    suspend fun stakingParams(request: QueryOuterClass.QueryParamsRequest): QueryOuterClass.QueryParamsResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).stakingParams(request)
    }

}