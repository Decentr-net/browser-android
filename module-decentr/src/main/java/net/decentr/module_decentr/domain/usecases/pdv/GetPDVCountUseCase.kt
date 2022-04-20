package net.decentr.module_decentr.domain.usecases.pdv

interface GetPDVCountUseCase {
    suspend operator fun invoke(address: String): Int
}