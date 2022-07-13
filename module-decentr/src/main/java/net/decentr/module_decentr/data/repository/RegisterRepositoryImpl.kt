package net.decentr.module_decentr.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.decentr.module_decentr.data.datasource.cerberus.RegisterDataSource
import net.decentr.module_decentr_domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val remoteRegisterDataSource: RegisterDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): RegisterRepository {

    override suspend fun register(address: String, email: String) {
        return withContext(ioDispatcher) {
            remoteRegisterDataSource.register(address, email)
        }
    }

    override suspend fun confirm(email: String, code: String) {
        return withContext(ioDispatcher) {
            remoteRegisterDataSource.confirm(email, code)
        }
    }

    override suspend fun trackInstall(address: String) {
        withContext(ioDispatcher) {
            remoteRegisterDataSource.trackInstall(address)
        }
    }
}