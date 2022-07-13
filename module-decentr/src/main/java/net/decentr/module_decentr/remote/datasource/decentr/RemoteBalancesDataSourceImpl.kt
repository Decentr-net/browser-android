package net.decentr.module_decentr.remote.datasource.decentr

import net.decentr.module_decentr.data.datasource.cerberus.BalancesDataSource
import net.decentr.module_decentr_domain.models.BalanceDEC
import net.decentr.module_decentr_domain.models.BalancePDV
import net.decentr.module_decentr.remote.services.DecentrService
import javax.inject.Inject

class RemoteBalancesDataSourceImpl @Inject constructor(
    private val decentrService: DecentrService
): BalancesDataSource {

    override suspend fun getBalancePDV(address: String): BalancePDV {
        val response = decentrService.getBalancePDV(address)
        return BalancePDV(response.balances.first().amount / 1000000)
    }

    override suspend fun getBalanceDEC(address: String): BalanceDEC {
        val response = decentrService.getBalanceDEC(address)
        return BalanceDEC(response.balance.dec)
    }


}