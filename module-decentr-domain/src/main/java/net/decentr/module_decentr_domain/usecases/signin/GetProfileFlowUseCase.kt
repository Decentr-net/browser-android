package net.decentr.module_decentr_domain.usecases.signin

import kotlinx.coroutines.flow.Flow
import net.decentr.module_decentr_domain.models.Profile

interface GetProfileFlowUseCase {
    suspend operator fun invoke(): Flow<Profile?>
}