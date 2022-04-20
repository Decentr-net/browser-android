package net.decentr.module_decentr.domain.usecases.pdv

import net.decentr.module_decentr.domain.repository.PDVRepository
import javax.inject.Inject

class GetPDVCountUseCaseImpl @Inject constructor(private val repository: PDVRepository): GetPDVCountUseCase {
    override suspend fun invoke(address: String): Int {
        return repository.getPDVCount(address)
    }
}