package net.decentr.module_decentr.domain.usecases.register

interface RegisterUseCase {
    suspend operator fun invoke(address: String, email: String)
}