package net.decentr.module_decentr_domain.usecases.pdv

import net.decentr.module_decentr_domain.models.PDV
import net.decentr.module_decentr_domain.repository.PDVRepository
import javax.inject.Inject

class CheckUnicPDVUseCaseImpl @Inject constructor(private val repository: PDVRepository):
    CheckUnicPDVUseCase {
    override suspend fun invoke(pdv: PDV): Boolean {
        return repository.checkUnicPDV(pdv)
    }
}