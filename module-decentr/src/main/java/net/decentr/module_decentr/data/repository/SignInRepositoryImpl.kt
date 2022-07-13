package net.decentr.module_decentr.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import net.decentr.module_decentr.data.datasource.cerberus.ProfileDataSource
import net.decentr.module_decentr_common.data.provider.KeysProvider
import net.decentr.module_decentr_domain.models.Profile
import net.decentr.module_decentr_domain.provider.MnemonicProvider
import net.decentr.module_decentr_domain.repository.SignInRepository
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val mnemonicProvider: MnemonicProvider,
    private val keysProvider: KeysProvider,
    private val localProfileDataSource: ProfileDataSource,
    private val remoteProfileDataSource: ProfileDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): SignInRepository {

    override suspend fun generateMnemonic(): String {
        return withContext(ioDispatcher) {
            mnemonicProvider.generateSeedPhrase()
        }
    }

    override suspend fun checkAddressInBlockchain(address: String): Boolean {
        return withContext(ioDispatcher) {
            remoteProfileDataSource.checkAddressInBlockchain(address)
        }
    }

    override suspend fun getProfile(address: String?): Profile? {
        return withContext(ioDispatcher) {
            if (address != null) {
                remoteProfileDataSource.getProfile(address)
            } else {
                localProfileDataSource.getProfile(null)
            }
        }
    }

    override suspend fun getProfileFlow(): Flow<Profile?> {
        return withContext(ioDispatcher) {
            localProfileDataSource.getProfileFlow()
        }
    }

    override suspend fun saveProfile(profile: Profile) {
        withContext(ioDispatcher) {
            localProfileDataSource.saveProfile(profile)
        }
    }

    override suspend fun removeProfile() {
        localProfileDataSource.removeProfile()
    }

    override suspend fun updateKeys(privKey: String, pubKey: String) {
        keysProvider.updateKeys(privKey, pubKey)
    }

    override suspend fun removeKeys() {
        keysProvider.skipKeys()
    }

}