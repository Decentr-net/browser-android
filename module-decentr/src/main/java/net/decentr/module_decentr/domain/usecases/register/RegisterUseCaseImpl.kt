package net.decentr.module_decentr.domain.usecases.register

import net.decentr.module_decentr.domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterUseCaseImpl @Inject constructor(private val repository: RegisterRepository): RegisterUseCase {
    override suspend fun invoke(address: String, email: String) {
        return repository.register(address, email)
    }
}