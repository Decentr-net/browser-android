package net.decentr.module_decentr.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BlockchainAccountResponse(
    @Expose
    val account: AccountResponse?
)

data class AccountResponse(
//    @Expose
//    @SerializedName("@type")
//    val type: String?,
    @Expose
    val address: String?,
//    @Expose
//    @SerializedName("pub_key")
//    val pubKey: String?,
    @Expose
    @SerializedName("account_number")
    val accountNumber: String?
)