package net.decentr.module_decentr.data.datasource.cerberus

import kotlinx.coroutines.flow.Flow
import net.decentr.module_decentr_domain.models.Profile

interface ProfileDataSource {
    suspend fun getProfile(address: String?): Profile?
    suspend fun checkAddressInBlockchain(address: String): Boolean
    suspend fun getProfileFlow(): Flow<Profile?>
    suspend fun saveProfile(profile: Profile)
    suspend fun removeProfile()
}