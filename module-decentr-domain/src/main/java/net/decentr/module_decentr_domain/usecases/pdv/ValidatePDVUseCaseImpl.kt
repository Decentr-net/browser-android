package net.decentr.module_decentr_domain.usecases.pdv

import net.decentr.module_decentr_domain.models.PDV
import net.decentr.module_decentr_domain.repository.PDVRepository
import javax.inject.Inject

class ValidatePDVUseCaseImpl @Inject constructor(private val repository: PDVRepository):
    ValidatePDVUseCase {

    override suspend fun invoke(pdv: List<PDV>): Pair<Boolean, List<Int>?> {
        return repository.validatePDV(pdv)
    }
}