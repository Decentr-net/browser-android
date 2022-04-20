package net.decentr.module_decentr.domain.usecases.pdv

import net.decentr.module_decentr.domain.models.PDV
import net.decentr.module_decentr.domain.repository.PDVRepository
import javax.inject.Inject

class SendPDVUseCaseImpl @Inject constructor(private val repository: PDVRepository): SendPDVUseCase {

    override suspend fun invoke(pdv: List<PDV>): Int {
        return repository.sendPDV(pdv)
    }
}