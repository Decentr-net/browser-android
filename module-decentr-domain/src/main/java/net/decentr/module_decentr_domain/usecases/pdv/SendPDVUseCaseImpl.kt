package net.decentr.module_decentr_domain.usecases.pdv

import net.decentr.module_decentr_domain.models.PDV
import net.decentr.module_decentr_domain.repository.PDVRepository
import javax.inject.Inject

class SendPDVUseCaseImpl @Inject constructor(private val repository: PDVRepository):
    SendPDVUseCase {

    override suspend fun invoke(pdv: List<PDV>): Int {
        return repository.sendPDV(pdv)
    }
}