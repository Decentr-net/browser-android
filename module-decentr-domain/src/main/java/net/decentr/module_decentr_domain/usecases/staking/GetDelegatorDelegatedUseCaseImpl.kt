package net.decentr.module_decentr_domain.usecases.staking

import net.decentr.module_decentr_domain.repository.StakingRepository
import javax.inject.Inject

class GetDelegatorDelegatedUseCaseImpl @Inject constructor(private val repository: StakingRepository): GetDelegatorDelegatedUseCase {
    override suspend fun invoke(address: String): List<Pair<String, String>> {
        return repository.getDelegatorDelegated(address)
    }
}