package net.decentr.module_decentr_domain.usecases.staking

import net.decentr.module_decentr_domain.models.staking.Validator
import net.decentr.module_decentr_domain.repository.StakingRepository
import javax.inject.Inject

class GetValidatorProfileUseCaseImpl @Inject constructor(private val repository: StakingRepository) :
    GetValidatorProfileUseCase {

    override suspend fun invoke(address: String): Validator {
        return repository.getValidator(address)
    }
}