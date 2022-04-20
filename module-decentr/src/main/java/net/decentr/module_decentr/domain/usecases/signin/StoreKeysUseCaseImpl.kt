package net.decentr.module_decentr.domain.usecases.signin

import net.decentr.module_decentr.domain.repository.SignInRepository
import javax.inject.Inject

class StoreKeysUseCaseImpl @Inject constructor(private val repository: SignInRepository): StoreKeysUseCase {

    override suspend fun invoke(privKey: String, pubKey: String) {
        repository.updateKeys(privKey = privKey, pubKey = pubKey)
    }
}