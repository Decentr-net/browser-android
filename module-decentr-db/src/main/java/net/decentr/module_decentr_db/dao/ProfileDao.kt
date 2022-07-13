package net.decentr.module_decentr_db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.decentr.module_decentr_db.entities.ProfileEntity

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Transaction
    @Query("SELECT * FROM Profile")
    fun getProfile(): ProfileEntity?

    @Transaction
    @Query("SELECT * FROM Profile")
    fun getProfileFlow(): Flow<ProfileEntity?>

    @Query("DELETE FROM Profile")
    suspend fun remove()
}