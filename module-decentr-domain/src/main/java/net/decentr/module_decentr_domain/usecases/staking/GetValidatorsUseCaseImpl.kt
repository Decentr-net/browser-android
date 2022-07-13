package net.decentr.module_decentr_domain.usecases.staking

import net.decentr.module_decentr_domain.models.staking.Validator
import net.decentr.module_decentr_domain.repository.StakingRepository
import javax.inject.Inject

class GetValidatorsUseCaseImpl @Inject constructor(private val repository: StakingRepository) :
    GetValidatorsUseCase {
    override suspend fun invoke(status: Validator.Status): List<Validator> {
        return repository.getValidators(status)
    }
}