package net.decentr.module_decentr_domain.usecases.pdv

import net.decentr.module_decentr_domain.models.PDV
import net.decentr.module_decentr_domain.repository.PDVRepository
import javax.inject.Inject

class RemovePDVUseCaseImpl @Inject constructor(private val repository: PDVRepository):
    RemovePDVUseCase {
    override suspend fun invoke(pdv: List<PDV>?) {
        repository.removePDV(pdv)
    }
}