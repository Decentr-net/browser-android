package net.decentr.module_decentr.remote.services

import net.decentr.module_decentr.remote.model.IdResponse
import net.decentr.module_decentr.remote.model.PDVRequest
import net.decentr.module_decentr.remote.model.ProfileResponse
import net.decentr.module_decentr.remote.model.ValidationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CerberusService {

    companion object {
        private const val SERVICE_VERSION = "v1"
        private const val SERVICE_PREFIX = "$SERVICE_VERSION"
    }

    @GET("$SERVICE_PREFIX/profiles")
    suspend fun getProfile(@Query("address") address: String?): List<ProfileResponse?>


    @POST("$SERVICE_PREFIX/pdv")
    suspend fun savePDV(@Body params: PDVRequest): IdResponse


    @POST("$SERVICE_PREFIX/pdv/validate")
    suspend fun validatePDV(@Body params: PDVRequest): ValidationResponse

}