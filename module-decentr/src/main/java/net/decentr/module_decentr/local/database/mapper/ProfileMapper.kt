package net.decentr.module_decentr.local.database.mapper

import net.decentr.module_decentr.domain.models.Profile
import net.decentr.module_decentr.local.database.entities.ProfileEntity

fun Profile.toEntity() = ProfileEntity(
    id = 0, address, avatar, isBanned, bio, birthDate, createdAt, emails, firstName, lastName, gender
)

fun ProfileEntity.toDomain() = Profile(
    address, avatar, isBanned, bio, birthDate, createdAt, emails, firstName, lastName, gender
)