package net.decentr.module_decentr.remote.datasource.cerberus

import kotlinx.coroutines.flow.Flow
import net.decentr.module_decentr.data.datasource.cerberus.ProfileDataSource
import net.decentr.module_decentr.domain.mappers.toDomain
import net.decentr.module_decentr.domain.models.Profile
import net.decentr.module_decentr.remote.services.CerberusService
import net.decentr.module_decentr.remote.services.DecentrService
import javax.inject.Inject

class RemoteProfileDataSourceImpl @Inject constructor(
    private val cerberusService: CerberusService,
    private val decentrService: DecentrService
): ProfileDataSource {

    override suspend fun getProfile(address: String?): Profile? {
        return address?.let { cerberusService.getProfile(it).first()?.toDomain() }
    }

    override suspend fun checkAddressInBlockchain(address: String): Boolean {
        return decentrService.checkAddressInBlockchain(address).account != null
    }

    override suspend fun getProfileFlow(): Flow<Profile> {
        throw IllegalStateException("You cannot get user flow in remote")
    }

    override suspend fun saveProfile(profile: Profile) {
        throw IllegalStateException("You cannot update user in remote")
    }

    override suspend fun removeProfile() {
        throw IllegalStateException("You cannot remove user in remote")
    }

}