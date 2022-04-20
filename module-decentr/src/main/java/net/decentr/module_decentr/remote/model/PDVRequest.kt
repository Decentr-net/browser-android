package net.decentr.module_decentr.remote.model

import com.google.gson.annotations.Expose
import net.decentr.module_decentr.domain.models.PDV
import java.util.*

data class PDVRequest(
    @Expose
    val version: String,
    @Expose
    val pdv: List<PDVResponse>
)

data class PDVResponse(
    @Expose
    val type: String,
    @Expose
    val domain: String? = null,
    @Expose
    val engine: String? = null,
    @Expose
    val query: String? = null,
    @Expose
    val timestamp: String? = null,
    @Expose
    val avatar: String? = null,
    @Expose
    val bio: String? = null,
    @Expose
    val birthday: String? = null,
    @Expose
    val emails: List<String>? = null,
    @Expose
    val gender: String? = null,
    @Expose
    val firstName: String? = null,
    @Expose
    val lastName: String? = null
)