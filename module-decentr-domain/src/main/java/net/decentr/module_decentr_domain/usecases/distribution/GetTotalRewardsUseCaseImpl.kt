package net.decentr.module_decentr_domain.usecases.distribution

import net.decentr.module_decentr_domain.models.staking.Reward
import net.decentr.module_decentr_domain.repository.DistributionRepository
import javax.inject.Inject

class GetTotalRewardsUseCaseImpl @Inject constructor(private val repository: DistributionRepository) :
    GetTotalRewardsUseCase {
    override suspend fun invoke(address: String): List<Reward> {
        return repository.getTotalRewards(address)
    }
}