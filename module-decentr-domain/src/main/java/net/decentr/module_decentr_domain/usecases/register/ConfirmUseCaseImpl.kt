package net.decentr.module_decentr_domain.usecases.register

import net.decentr.module_decentr_domain.repository.RegisterRepository
import javax.inject.Inject

class ConfirmUseCaseImpl @Inject constructor(private val repository: RegisterRepository):
    ConfirmUseCase {
    override suspend fun invoke(email: String, code: String) {
        return repository.confirm(email, code)
    }
}