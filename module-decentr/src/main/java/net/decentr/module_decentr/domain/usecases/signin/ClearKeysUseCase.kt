package net.decentr.module_decentr.domain.usecases.signin

interface ClearKeysUseCase {
    suspend operator fun invoke(privKey: String, pubKey: String)
}