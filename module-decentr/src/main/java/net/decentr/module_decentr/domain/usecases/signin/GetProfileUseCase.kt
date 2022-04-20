package net.decentr.module_decentr.domain.usecases.signin

import net.decentr.module_decentr.domain.models.Profile

interface GetProfileUseCase {
    suspend operator fun invoke(address: String?): Profile?
}