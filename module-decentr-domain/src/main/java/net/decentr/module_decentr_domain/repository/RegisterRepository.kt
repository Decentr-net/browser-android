package net.decentr.module_decentr_domain.repository

interface RegisterRepository {
    suspend fun register(address: String, email: String)
    suspend fun confirm(email: String, code: String)
    suspend fun trackInstall(address: String)
}