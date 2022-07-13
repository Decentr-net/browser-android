package net.decentr.module_decentr.data.datasource.cerberus

import net.decentr.module_decentr_domain.models.BalanceDEC
import net.decentr.module_decentr_domain.models.BalancePDV

interface BalancesDataSource {
    suspend fun getBalancePDV(address: String): BalancePDV
    suspend fun getBalanceDEC(address: String): BalanceDEC
}