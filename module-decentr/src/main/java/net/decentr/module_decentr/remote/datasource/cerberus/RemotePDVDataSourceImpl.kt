package net.decentr.module_decentr.remote.datasource.cerberus

import net.decentr.module_decentr.data.datasource.cerberus.PDVDataSource
import net.decentr.module_decentr.remote.mapper.toRemote
import net.decentr.module_decentr.remote.model.PDVRequest
import net.decentr.module_decentr.remote.services.CerberusService
import net.decentr.module_decentr_domain.models.PDV
import javax.inject.Inject

class RemotePDVDataSourceImpl @Inject constructor(
    private val cerberusService: CerberusService
): PDVDataSource {

    override suspend fun sendPDV(pdv: List<PDV>): Int {
        val requestBody = PDVRequest("v1", pdv.map { it.toRemote() })
        return cerberusService.savePDV(requestBody).id
    }

    override suspend fun validatePDV(pdv: List<PDV>): Pair<Boolean, List<Int>?> {
        val requestBody = PDVRequest("v1", pdv.map { it.toRemote() })
        val response = cerberusService.validatePDV(requestBody)
        return response.valid to response.invalidPdv
    }

    override suspend fun savePDV(pdv: List<PDV>): Int {
        throw IllegalStateException("You cannot save PDV in remote")
    }

    override suspend fun checkUnicPDV(pdv: PDV): Boolean {
        throw IllegalStateException("You cannot check unic PDV in remote")
    }

    override suspend fun getAllPDV(address: String?): List<PDV>? {
        throw IllegalStateException("You cannot get PDV in remote")
    }

    override suspend fun getPDVCount(address: String): Int {
        throw IllegalStateException("You cannot get PDV count in remote")
    }

    override suspend fun removePDV(pdv: List<PDV>?) {
        throw IllegalStateException("You cannot remove PDV in remote")
    }
}