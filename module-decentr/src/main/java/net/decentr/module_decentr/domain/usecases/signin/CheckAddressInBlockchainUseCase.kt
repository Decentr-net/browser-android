package net.decentr.module_decentr.domain.usecases.signin

interface CheckAddressInBlockchainUseCase {
    suspend operator fun invoke(address: String): Boolean
}