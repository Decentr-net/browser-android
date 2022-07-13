package net.decentr.module_decentr_domain.usecases.pdv

import net.decentr.module_decentr_domain.models.PDV

interface GetAllPDVUseCase {
    suspend operator fun invoke(address: String? = null): List<PDV>
}