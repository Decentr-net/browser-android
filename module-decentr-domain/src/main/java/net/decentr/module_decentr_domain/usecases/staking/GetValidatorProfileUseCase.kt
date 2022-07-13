package net.decentr.module_decentr_domain.usecases.staking

import net.decentr.module_decentr_domain.models.staking.Validator

interface GetValidatorProfileUseCase {
    suspend operator fun invoke(address: String): Validator
}