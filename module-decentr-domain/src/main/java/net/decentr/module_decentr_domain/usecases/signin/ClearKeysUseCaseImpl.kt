package net.decentr.module_decentr_domain.usecases.signin

import net.decentr.module_decentr_domain.repository.SignInRepository
import javax.inject.Inject

class ClearKeysUseCaseImpl @Inject constructor(private val repository: SignInRepository):
    ClearKeysUseCase {
    override suspend fun invoke(privKey: String, pubKey: String) {
        repository.removeKeys()
    }
}