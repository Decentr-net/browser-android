package net.decentr.module_decentr.domain.usecases.pdv

import net.decentr.module_decentr.domain.models.PDV

interface GetAllPDVUseCase {
    suspend operator fun invoke(address: String? = null): List<PDV>
}