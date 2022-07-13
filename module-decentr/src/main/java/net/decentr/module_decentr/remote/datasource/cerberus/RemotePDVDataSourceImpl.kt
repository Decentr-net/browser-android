package net.decentr.module_decentr.remote.datasource.cerberus

import net.decentr.module_decentr.data.datasource.cerberus.PDVDataSource
import net.decentr.module_decentr.remote.mapper.toRemote
import net.decentr.module_decentr.remote.model.PDVRequest
import net.decentr.module_decentr.remote.services.CerberusService
import javax.inject.Inject

class RemotePDVDataSourceImpl @Inject constructor(
    private val cerberusService: CerberusService
): PDVDataSource {

    override suspend fun sendPDV(pdv: List<net.decentr.module_decentr_domain.models.PDV>): Int {
        val requestBody = PDVRequest("v1", pdv.map { it.toRemote() })
        return cerberusService.savePDV(requestBody).id
    }

    override suspend fun savePDV(pdv: List<net.decentr.module_decentr_domain.models.PDV>): Int {
        throw IllegalStateException("You cannot save PDV in remote")
    }

    override suspend fun checkUnicPDV(pdv: net.decentr.module_decentr_domain.models.PDV): Boolean {
        throw IllegalStateException("You cannot check unic PDV in remote")
    }

    override suspend fun getAllPDV(address: String?): List<net.decentr.module_decentr_domain.models.PDV>? {
        throw IllegalStateException("You cannot get PDV in remote")
    }

    override suspend fun getPDVCount(address: String): Int {
        throw IllegalStateException("You cannot get PDV count in remote")
    }

    override suspend fun removePDV(pdv: List<net.decentr.module_decentr_domain.models.PDV>?) {
        throw IllegalStateException("You cannot remove PDV in remote")
    }
}