package net.decentr.module_decentr_domain.repository

import net.decentr.module_decentr_domain.models.staking.Reward

interface DistributionRepository {
    suspend fun getTotalRewards(address: String): List<Reward>
    fun closeChannel()
}