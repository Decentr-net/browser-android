package net.decentr.module_decentr_domain.usecases.staking

import net.decentr.module_decentr_domain.models.staking.Validator

interface GetValidatorsUseCase {
    suspend operator fun invoke(status: Validator.Status): List<Validator>
}