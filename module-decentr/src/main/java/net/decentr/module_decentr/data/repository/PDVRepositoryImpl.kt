package net.decentr.module_decentr.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.decentr.module_decentr.data.datasource.cerberus.PDVDataSource
import net.decentr.module_decentr_domain.models.PDV
import net.decentr.module_decentr_domain.repository.PDVRepository
import javax.inject.Inject

class PDVRepositoryImpl @Inject constructor(
    private val localPDVDataSource: PDVDataSource,
    private val remotePDVDataSource: PDVDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PDVRepository {

    override suspend fun sendPDV(pdv: List<PDV>): Int {
        return withContext(ioDispatcher) {
            remotePDVDataSource.sendPDV(pdv)
        }
    }

    override suspend fun savePDV(pdv: List<PDV>): Int {
        return withContext(ioDispatcher) {
            localPDVDataSource.savePDV(pdv)
        }
    }

    override suspend fun checkUnicPDV(pdv: PDV): Boolean {
        return withContext(ioDispatcher) {
            localPDVDataSource.checkUnicPDV(pdv)
        }
    }

    override suspend fun getPDVCount(address: String): Int {
        return withContext(ioDispatcher) {
            localPDVDataSource.getPDVCount(address)
        }
    }

    override suspend fun getPDV(address: String?): List<PDV> {
        return withContext(ioDispatcher) {
            localPDVDataSource.getAllPDV(address) ?: emptyList()
        }
    }

    override suspend fun removePDV(list: List<PDV>?) {
        withContext(ioDispatcher) {
            localPDVDataSource.removePDV(list)
        }
    }
}