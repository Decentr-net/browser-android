package net.decentr.module_decentr_domain.usecases.signin

import net.decentr.module_decentr_domain.repository.SignInRepository
import javax.inject.Inject

class RemoveUserUseCaseImpl @Inject constructor(private val repository: SignInRepository):
    RemoveUserUseCase {
    override suspend fun invoke() {
        repository.removeProfile()
        repository.removeKeys()
    }
}