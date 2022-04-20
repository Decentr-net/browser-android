package net.decentr.module_decentr.domain.usecases.pdv

import net.decentr.module_decentr.domain.models.PDV
import net.decentr.module_decentr.domain.repository.PDVRepository
import javax.inject.Inject

class RemovePDVUseCaseImpl @Inject constructor(private val repository: PDVRepository): RemovePDVUseCase {
    override suspend fun invoke(pdv: List<PDV>?) {
        repository.removePDV(pdv)
    }
}