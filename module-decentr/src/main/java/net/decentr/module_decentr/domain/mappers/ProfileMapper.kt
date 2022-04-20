package net.decentr.module_decentr.domain.mappers

import net.decentr.module_decentr.domain.models.Profile
import net.decentr.module_decentr.remote.model.ProfileResponse

fun ProfileResponse.toDomain() = Profile(
    address ?: String(), avatar, isBanned, bio, birthDate, createdAt, emails, firstName ?: String(), lastName ?: String(), gender
)