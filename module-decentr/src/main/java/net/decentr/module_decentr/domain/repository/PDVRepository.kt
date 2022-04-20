package net.decentr.module_decentr.domain.repository

import net.decentr.module_decentr.domain.models.PDV

interface PDVRepository {
    suspend fun sendPDV(pdv: List<PDV>): Int
    suspend fun savePDV(pdv: List<PDV>): Int
    suspend fun checkUnicPDV(pdv: PDV): Boolean
    suspend fun getPDVCount(address: String): Int
    suspend fun getPDV(address: String?): List<PDV>
    suspend fun removePDV(list: List<PDV>?)
}