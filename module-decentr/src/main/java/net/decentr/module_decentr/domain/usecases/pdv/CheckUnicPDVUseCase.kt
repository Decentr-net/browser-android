package net.decentr.module_decentr.domain.usecases.pdv

import net.decentr.module_decentr.domain.models.PDV

interface CheckUnicPDVUseCase {
    suspend operator fun invoke(pdv: PDV): Boolean
}