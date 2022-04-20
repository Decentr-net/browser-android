package net.decentr.module_decentr.remote.model

import com.google.gson.annotations.Expose

data class RegisterRequest(
    @Expose
    val address: String,
    @Expose
    val email: String
)

data class ConfirmRequest(
    @Expose
    val email: String,
    @Expose
    val code: String
)

//{
//    "address": "string",
//    "email": "user@example.com",
//    "recaptchaResponse": "string",
//    "referralCode": "string"
//}