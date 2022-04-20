package net.decentr.module_decentr.domain.usecases.pdv

import net.decentr.module_decentr.domain.models.PDV

interface SendPDVUseCase {
    suspend operator fun invoke(pdv: List<PDV>): Int
}