package net.decentr.module_decentr_staking.remote.services

import cosmos.distribution.v1beta1.DistributionQuery
import cosmos.distribution.v1beta1.QueryGrpcKt
import io.grpc.Channel
import io.grpc.ManagedChannel
import io.grpc.okhttp.OkHttpChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import net.decentr.module_decentr_domain.errors.DecException
import net.decentr.module_decentr_domain.errors.ERROR_CODE_GRPC_CHANNEL_CLOSING
import net.decentr.module_decentr_staking.remote.utils.GrpcLoggingInterceptorKt
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DistributionService @Inject constructor() {

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

    suspend fun communityPool(request: DistributionQuery.QueryCommunityPoolRequest): DistributionQuery.QueryCommunityPoolResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).communityPool(request)
    }

    suspend fun distributionParams(request: DistributionQuery.QueryParamsRequest): DistributionQuery.QueryParamsResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).distributionParams(request)
    }

    suspend fun delegationRewards(request: DistributionQuery.QueryDelegationRewardsRequest): DistributionQuery.QueryDelegationRewardsResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).delegationRewards(request)
    }

    suspend fun delegationTotalRewards(request: DistributionQuery.QueryDelegationTotalRewardsRequest): DistributionQuery.QueryDelegationTotalRewardsResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).delegationTotalRewards(request)
    }

    suspend fun delegatorWithdrawAddress(request: DistributionQuery.QueryDelegatorWithdrawAddressRequest): DistributionQuery.QueryDelegatorWithdrawAddressResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).delegatorWithdrawAddress(request)
    }

    suspend fun validatorCommission(request: DistributionQuery.QueryValidatorCommissionRequest): DistributionQuery.QueryValidatorCommissionResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).validatorCommission(request)
    }

    suspend fun validatorOutstandingRewards(request: DistributionQuery.QueryValidatorOutstandingRewardsRequest): DistributionQuery.QueryValidatorOutstandingRewardsResponse {
        return QueryGrpcKt.QueryCoroutineStub(channel).validatorOutstandingRewards(request)
    }


}