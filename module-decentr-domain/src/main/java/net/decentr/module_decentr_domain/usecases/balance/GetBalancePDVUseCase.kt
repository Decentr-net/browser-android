package net.decentr.module_decentr_domain.usecases.balance

import net.decentr.module_decentr_domain.models.BalancePDV

interface GetBalancePDVUseCase {
    suspend operator fun invoke(address: String): BalancePDV
}