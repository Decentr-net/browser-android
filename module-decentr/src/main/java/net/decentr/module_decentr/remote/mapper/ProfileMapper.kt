package net.decentr.module_decentr.remote.mapper

import net.decentr.module_decentr.remote.model.ProfileResponse
import net.decentr.module_decentr_domain.models.Profile

fun ProfileResponse.toDomain() = Profile(
    address ?: String(), avatar, isBanned, bio, birthDate, createdAt, emails, firstName ?: String(), lastName ?: String(), gender
)