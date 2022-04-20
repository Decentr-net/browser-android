package net.decentr.module_decentr.domain.usecases.balance

import net.decentr.module_decentr.domain.models.BalancePDV

interface GetBalancePDVUseCase {
    suspend operator fun invoke(address: String): BalancePDV
}