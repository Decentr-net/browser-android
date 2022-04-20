package net.decentr.module_decentr.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.decentr.module_decentr.data.datasource.cerberus.BalancesDataSource
import net.decentr.module_decentr.domain.models.BalanceDEC
import net.decentr.module_decentr.domain.models.BalancePDV
import net.decentr.module_decentr.domain.repository.BalanceRepository
import javax.inject.Inject

class BalanceRepositoryImpl @Inject constructor(
    private val remotePDVDataSource: BalancesDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): BalanceRepository {

    override suspend fun getBalancePDV(address: String): BalancePDV {
        return withContext(ioDispatcher) {
            remotePDVDataSource.getBalancePDV(address)
        }
    }

    override suspend fun getBalanceDEC(address: String): BalanceDEC {
        return withContext(ioDispatcher) {
            remotePDVDataSource.getBalanceDEC(address)
        }
    }

}