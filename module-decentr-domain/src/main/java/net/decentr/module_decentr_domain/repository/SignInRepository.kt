package net.decentr.module_decentr_domain.repository

import kotlinx.coroutines.flow.Flow
import net.decentr.module_decentr_domain.models.Profile

interface SignInRepository {
    suspend fun generateMnemonic(): String
    suspend fun checkAddressInBlockchain(address: String): Boolean
    suspend fun getProfile(address: String?): Profile?
    suspend fun getProfileFlow(): Flow<Profile?>

    suspend fun saveProfile(profile: Profile)
    suspend fun removeProfile()

    suspend fun updateKeys(privKey: String, pubKey: String)
    suspend fun removeKeys()
}