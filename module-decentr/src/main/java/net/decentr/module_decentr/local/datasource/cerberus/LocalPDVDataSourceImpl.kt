package net.decentr.module_decentr.local.datasource.cerberus

import net.decentr.module_decentr.data.datasource.cerberus.PDVDataSource
import net.decentr.module_decentr.local.mapper.toDomain
import net.decentr.module_decentr.local.mapper.toEntity
import net.decentr.module_decentr_db.ProjectDatabase
import net.decentr.module_decentr_db.dao.PDVDao
import net.decentr.module_decentr_domain.models.PDV
import net.decentr.module_decentr_domain.models.PDVHistory
import javax.inject.Inject

class LocalPDVDataSourceImpl @Inject constructor(
    private val pdvDao: PDVDao,
    private val database: ProjectDatabase,
): PDVDataSource {

    override suspend fun sendPDV(pdv: List<PDV>): Int {
        throw IllegalStateException("You cannot send pdv local")
    }

    override suspend fun savePDV(pdv: List<PDV>): Int {
        pdvDao.insertPDVList(pdv.map { it.toEntity() })
        return pdvDao.getPDVCount(pdv.first().address!!) ?: 0
    }

    override suspend fun checkUnicPDV(pdv: PDV): Boolean {
        val pdvHistory = pdv as? PDVHistory
        val pdvWithSameParams = pdvDao.checkUnicPDV(pdvHistory?.engine ?: String(), pdvHistory?.query ?: String())
        return pdvWithSameParams.isNullOrEmpty()
    }

    override suspend fun getAllPDV(address: String?): List<PDV>? {
        return if (address.isNullOrEmpty()) pdvDao.getPDV()?.map { it.toDomain() }
        else pdvDao.getPDVByAddress(address)?.map { it.toDomain() }
    }

    override suspend fun getPDVCount(address: String): Int {
        return pdvDao.getPDVCount(address) ?: 0
    }

    override suspend fun removePDV(pdv: List<PDV>?) {
        if (!pdv.isNullOrEmpty()) {
            when {
                ((pdv.first() as? PDVHistory)?.id != null) && ((pdv.last() as? PDVHistory)?.id != null) -> {
                    val idFrom = (pdv.last() as PDVHistory).id
                    val idTo = (pdv.first() as PDVHistory).id
                    pdvDao.removeByIDs(idFrom!!, idTo!!)
                }
                else -> {
                    pdv.forEach {
                        pdvDao.removeSentList(it.toEntity())
                    }
                }
            }
        } else pdvDao.remove()
    }
}