package net.decentr.module_decentr_domain.repository

import net.decentr.module_decentr_domain.models.BalanceDEC
import net.decentr.module_decentr_domain.models.BalancePDV

interface BalanceRepository {
    suspend fun getBalancePDV(address: String): BalancePDV
    suspend fun getBalanceDEC(address: String): BalanceDEC
}