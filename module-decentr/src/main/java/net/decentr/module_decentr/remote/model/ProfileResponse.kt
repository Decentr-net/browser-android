package net.decentr.module_decentr.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @Expose
    @SerializedName("address")
    val address: String?,
    @Expose
    @SerializedName("avatar")
    val avatar: String?,
    @Expose
    @SerializedName("banned")
    val isBanned: Boolean,
    @Expose
    @SerializedName("bio")
    val bio: String?,
    @Expose
    @SerializedName("birthday")
    val birthDate: String?,
    @Expose
    @SerializedName("createdAt")
    val createdAt: Long?,
    @Expose
    @SerializedName("emails")
    val emails: List<String>?,
    @Expose
    @SerializedName("firstName")
    val firstName: String?,
    @Expose
    @SerializedName("lastName")
    val lastName: String?,
    @Expose
    @SerializedName("gender")
    val gender: String?
)