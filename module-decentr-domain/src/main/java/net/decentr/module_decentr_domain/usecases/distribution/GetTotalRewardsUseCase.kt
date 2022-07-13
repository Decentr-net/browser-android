package net.decentr.module_decentr_domain.usecases.distribution

import net.decentr.module_decentr_domain.models.staking.Reward

interface GetTotalRewardsUseCase {
    suspend operator fun invoke(address: String): List<Reward>
}