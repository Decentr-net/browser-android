package net.decentr.module_decentr.remote.datasource.vulcan

import net.decentr.module_decentr.data.datasource.cerberus.RegisterDataSource
import net.decentr.module_decentr.remote.model.ConfirmRequest
import net.decentr.module_decentr.remote.model.RegisterRequest
import net.decentr.module_decentr.remote.services.VulcanService
import javax.inject.Inject

class RemoteRegisterDataSourceImpl @Inject constructor(
    private val vulcanService: VulcanService
): RegisterDataSource {

    override suspend fun register(address: String, email: String) {
        return vulcanService.register(RegisterRequest(address, email))
    }

    override suspend fun confirm(email: String, code: String) {
        return vulcanService.confirm(ConfirmRequest(email, code))
    }

    override suspend fun trackInstall(address: String) {
        vulcanService.trackInstall(address)
    }
}