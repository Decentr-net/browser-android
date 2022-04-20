package net.decentr.module_decentr.domain.usecases.signin

import net.decentr.module_decentr.domain.models.Profile
import net.decentr.module_decentr.domain.repository.SignInRepository
import javax.inject.Inject

class GetProfileUseCaseImpl @Inject constructor(val repository: SignInRepository): GetProfileUseCase {
    override suspend fun invoke(address: String?): Profile? {
        return repository.getProfile(address)
    }
}