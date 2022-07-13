package net.decentr.module_decentr_domain.usecases.signin

interface StoreKeysUseCase {
    suspend operator fun invoke(privKey: String, pubKey: String)
}