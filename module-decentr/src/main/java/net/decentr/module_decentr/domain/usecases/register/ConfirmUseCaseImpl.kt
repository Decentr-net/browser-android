package net.decentr.module_decentr.domain.usecases.register

import net.decentr.module_decentr.domain.repository.RegisterRepository
import javax.inject.Inject

class ConfirmUseCaseImpl @Inject constructor(private val repository: RegisterRepository): ConfirmUseCase {
    override suspend fun invoke(email: String, code: String) {
        return repository.confirm(email, code)
    }
}