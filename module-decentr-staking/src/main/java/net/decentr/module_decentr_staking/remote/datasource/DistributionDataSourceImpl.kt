package net.decentr.module_decentr_staking.remote.datasource

import cosmos.distribution.v1beta1.DistributionQuery
import net.decentr.module_decentr_domain.models.staking.Reward
import net.decentr.module_decentr_staking.data.datasource.DistributionDataSource
import net.decentr.module_decentr_staking.data.utils.configNetworkHosts
import net.decentr.module_decentr_staking.data.utils.configNetworkPort
import net.decentr.module_decentr_staking.remote.mapper.toDomain
import net.decentr.module_decentr_staking.remote.services.DistributionActionsService
import net.decentr.module_decentr_staking.remote.services.DistributionService
import javax.inject.Inject

class DistributionDataSourceImpl @Inject constructor(
    private val service: DistributionService,
    private val actionsService: DistributionActionsService
): DistributionDataSource {

    init {
        val host = configNetworkHosts.random()
        val port = configNetworkPort
        service.init(host, port)
        actionsService.init(host, port)
    }

    override suspend fun delegatorTotalRewards(address: String): List<Reward> {
        val request = DistributionQuery.QueryDelegationTotalRewardsRequest.newBuilder().setDelegatorAddress(address).build()
        return service.delegationTotalRewards(request).rewardsList.filter { !it.rewardList.isNullOrEmpty() }.map { it.toDomain() }
    }

    override fun closeChannel() {
        service.closeChannel()
        actionsService.closeChannel()
    }
}