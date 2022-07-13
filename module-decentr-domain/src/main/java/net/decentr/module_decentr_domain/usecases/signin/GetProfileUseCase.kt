package net.decentr.module_decentr_domain.usecases.signin

import net.decentr.module_decentr_domain.models.Profile

interface GetProfileUseCase {
    suspend operator fun invoke(address: String?): Profile?
}