package net.decentr.module_decentr_domain.usecases.pdv

import net.decentr.module_decentr_domain.models.PDV
import net.decentr.module_decentr_domain.repository.PDVRepository
import javax.inject.Inject

class GetAllPDVUseCaseImpl @Inject constructor(private val repository: PDVRepository):
    GetAllPDVUseCase {
    override suspend fun invoke(address: String?): List<PDV> {
        return repository.getPDV(address)
    }
}