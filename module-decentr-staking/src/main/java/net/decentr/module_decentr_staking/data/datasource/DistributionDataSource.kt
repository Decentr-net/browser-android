package net.decentr.module_decentr_staking.data.datasource

import net.decentr.module_decentr_domain.models.staking.Reward

interface DistributionDataSource {
    suspend fun delegatorTotalRewards(address: String): List<Reward>
    fun closeChannel()
}