package net.decentr.module_decentr.remote.model

import com.google.gson.annotations.Expose

data class ValidationResponse(
    @Expose
    val invalidPdv: List<Int>?,
    @Expose
    val valid: Boolean
)