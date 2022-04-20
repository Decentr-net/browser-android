package net.decentr.module_decentr.domain.usecases.pdv

import net.decentr.module_decentr.domain.models.PDV
import net.decentr.module_decentr.domain.repository.PDVRepository
import javax.inject.Inject

class SavePDVUseCaseImpl @Inject constructor(private val repository: PDVRepository) :
    SavePDVUseCase {

    override suspend fun invoke(pdv: List<PDV>): Int {
        return repository.savePDV(pdv)
    }
}