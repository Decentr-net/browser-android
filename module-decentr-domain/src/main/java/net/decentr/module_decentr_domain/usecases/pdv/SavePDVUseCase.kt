package net.decentr.module_decentr_domain.usecases.pdv

import net.decentr.module_decentr_domain.models.PDV

interface SavePDVUseCase {
    suspend operator fun invoke(pdv: List<PDV>): Int
}