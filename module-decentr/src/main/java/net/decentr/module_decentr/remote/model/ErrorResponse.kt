package net.decentr.module_decentr.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//{
//    "error": "string",
//}

data class ErrorResponse(
        @Expose
        @SerializedName("error")
        val message: String,
        @Expose
        @SerializedName("code")
        val code: Int?
)