package net.decentr.module_decentr.local.datasource.cerberus

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import net.decentr.module_decentr.data.datasource.cerberus.ProfileDataSource
import net.decentr.module_decentr.local.mapper.toDomain
import net.decentr.module_decentr.local.mapper.toEntity
import net.decentr.module_decentr_db.ProjectDatabase
import net.decentr.module_decentr_db.dao.ProfileDao
import net.decentr.module_decentr_domain.models.Profile
import javax.inject.Inject

class LocalProfileDataSourceImpl @Inject constructor(
    private val profileDao: ProfileDao,
    private val database: ProjectDatabase,
) : ProfileDataSource {

    override suspend fun getProfile(address: String?): Profile? {
        return profileDao.getProfile()?.toDomain()
    }

    override suspend fun checkAddressInBlockchain(address: String): Boolean {
        throw IllegalStateException("You cannot check address in local")
    }

    override suspend fun getProfileFlow(): Flow<Profile?> {
        return profileDao.getProfileFlow().transform { profileEntity ->
            emit(profileEntity?.toDomain())
        }
    }

    override suspend fun saveProfile(profile: Profile) {
        database.withTransaction {
            profileDao.insertProfile(profile.toEntity())
        }
    }

    override suspend fun removeProfile() {
        profileDao.remove()
    }
}