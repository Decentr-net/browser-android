package net.decentr.module_decentr.remote.model

import com.google.gson.annotations.Expose

data class BalancePDVResponse(
    @Expose
    val balances: List<BalancePDVObjectResponse>
)

data class BalancePDVObjectResponse(
    @Expose
    val denom: String,
    @Expose
    val amount: Double
)