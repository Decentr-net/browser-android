package net.decentr.module_decentr_domain.usecases.staking

import net.decentr.module_decentr_domain.repository.StakingRepository
import javax.inject.Inject

class GetPoolUseCaseImpl @Inject constructor(private val repository: StakingRepository) :
    GetPoolUseCase {

    override suspend fun invoke(): Pair<String, String> {
        return repository.getPool()
    }
}