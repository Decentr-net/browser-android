package net.decentr.module_decentr.remote.services

import net.decentr.module_decentr.remote.model.ConfirmRequest
import net.decentr.module_decentr.remote.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface VulcanService {

    companion object {
        private const val SERVICE_VERSION = "v1"
        private const val SERVICE_PREFIX = "$SERVICE_VERSION"
    }

    @POST("$SERVICE_PREFIX/register")
    suspend fun register(@Body params: RegisterRequest)

    @POST("$SERVICE_PREFIX/confirm")
    suspend fun confirm(@Body params: ConfirmRequest)

    @POST("$SERVICE_PREFIX/referral/track/install/{address}")
    suspend fun trackInstall(@Path("address") address: String)
}