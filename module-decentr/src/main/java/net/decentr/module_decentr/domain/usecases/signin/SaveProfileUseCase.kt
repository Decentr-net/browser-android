package net.decentr.module_decentr.domain.usecases.signin

import net.decentr.module_decentr.domain.models.Profile

interface SaveProfileUseCase {
    suspend operator fun invoke(profile: Profile)
}