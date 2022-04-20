package net.decentr.module_decentr.remote.model

import com.google.gson.annotations.Expose

data class BalanceDECResponse(
    @Expose
    val balance: BalanceDECObjectResponse
)

data class BalanceDECObjectResponse(
    @Expose
    val dec: Double
)