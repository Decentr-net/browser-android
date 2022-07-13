package net.decentr.module_decentr_domain.models

data class Profile(
    val address: String,
    val avatar: String?,
    val isBanned: Boolean,
    val bio: String?,
    val birthDate: String?,
    val createdAt: Long?,
    val emails: List<String>?,
    val firstName: String,
    val lastName: String,
    val gender: String?
)