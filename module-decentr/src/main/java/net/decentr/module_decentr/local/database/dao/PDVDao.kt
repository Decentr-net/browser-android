package net.decentr.module_decentr.local.database.dao

import androidx.room.*
import net.decentr.module_decentr.local.database.entities.PDVEntity


@Dao
interface PDVDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPDV(pdv: PDVEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPDVList(pdv: List<PDVEntity>)

    @Query("SELECT COUNT(*) FROM PDV WHERE address = :address OR (address is null)")
    fun getPDVCount(address: String): Int?

    @Query("SELECT * FROM PDV")
    fun getPDV(): List<PDVEntity>?

    @Query("SELECT * FROM PDV WHERE address = :address")
    fun getPDVByAddress(address: String): List<PDVEntity>?

    @Query("SELECT * FROM PDV WHERE engine = :engine AND `query` = :query")
    suspend fun checkUnicPDV(engine: String, query: String): List<PDVEntity>?

    @Query("DELETE FROM PDV")
    suspend fun remove()

    @Query("DELETE FROM PDV WHERE id BETWEEN :idFrom AND :idTo")
    suspend fun removeByIDs(idFrom: Int, idTo: Int)

    @Delete
    suspend fun removeSentList(pdv: PDVEntity)
}