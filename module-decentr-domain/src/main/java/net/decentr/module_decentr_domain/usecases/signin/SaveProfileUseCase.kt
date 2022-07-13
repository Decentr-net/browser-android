package net.decentr.module_decentr_domain.usecases.signin

import net.decentr.module_decentr_domain.models.Profile

interface SaveProfileUseCase {
    suspend operator fun invoke(profile: Profile)
}