package net.decentr.module_decentr.domain.usecases.register

interface ConfirmUseCase {
    suspend operator fun invoke(email: String, code: String)
}