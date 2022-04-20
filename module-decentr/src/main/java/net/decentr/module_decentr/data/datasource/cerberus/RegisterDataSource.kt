package net.decentr.module_decentr.data.datasource.cerberus

interface RegisterDataSource {
    suspend fun register(address: String, email: String)
    suspend fun confirm(email: String, code: String)
    suspend fun trackInstall(address: String)
}