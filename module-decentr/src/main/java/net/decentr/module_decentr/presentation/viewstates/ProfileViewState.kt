package net.decentr.module_decentr.presentation.viewstates

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileViewState(
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
): Parcelable