package net.decentr.module_decentr_domain.usecases.pdv

import net.decentr.module_decentr_domain.models.PDV

interface CheckUnicPDVUseCase {
    suspend operator fun invoke(pdv: PDV): Boolean
}