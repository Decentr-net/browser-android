package net.decentr.module_decentr.domain.usecases.signin

import kotlinx.coroutines.flow.Flow
import net.decentr.module_decentr.domain.models.Profile
import net.decentr.module_decentr.domain.repository.SignInRepository
import javax.inject.Inject

class GetProfileFlowUseCaseImpl @Inject constructor(private val repository: SignInRepository): GetProfileFlowUseCase {
    override suspend fun invoke(): Flow<Profile?> {
        return repository.getProfileFlow()
    }
}