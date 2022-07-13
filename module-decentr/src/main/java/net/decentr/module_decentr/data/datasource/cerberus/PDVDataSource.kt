package net.decentr.module_decentr.data.datasource.cerberus

import net.decentr.module_decentr_domain.models.PDV

interface PDVDataSource {
    suspend fun sendPDV(pdv: List<PDV>): Int
    suspend fun validatePDV(pdv: List<PDV>): Pair<Boolean, List<Int>?>
    suspend fun savePDV(pdv: List<PDV>): Int
    suspend fun checkUnicPDV(pdv: PDV): Boolean
    suspend fun getAllPDV(address: String?): List<PDV>?
    suspend fun getPDVCount(address: String): Int
    suspend fun removePDV(pdv: List<PDV>?)
}