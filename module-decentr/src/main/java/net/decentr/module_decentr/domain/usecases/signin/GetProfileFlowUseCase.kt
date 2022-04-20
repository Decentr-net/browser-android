package net.decentr.module_decentr.domain.usecases.signin

import kotlinx.coroutines.flow.Flow
import net.decentr.module_decentr.domain.models.Profile

interface GetProfileFlowUseCase {
    suspend operator fun invoke(): Flow<Profile?>
}