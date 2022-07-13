package net.decentr.module_decentr_domain.usecases.signin

import net.decentr.module_decentr_domain.repository.SignInRepository
import javax.inject.Inject

class CheckAddressInBlockchainUseCaseImpl @Inject constructor(private val repository: SignInRepository):
    CheckAddressInBlockchainUseCase {
    override suspend fun invoke(address: String): Boolean {
        return repository.checkAddressInBlockchain(address)
    }
}