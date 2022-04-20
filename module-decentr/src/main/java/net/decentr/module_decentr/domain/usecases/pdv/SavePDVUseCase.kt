package net.decentr.module_decentr.domain.usecases.pdv

import net.decentr.module_decentr.domain.models.PDV

interface SavePDVUseCase {
    suspend operator fun invoke(pdv: List<PDV>): Int
}