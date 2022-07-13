package net.decentr.module_decentr.local.mapper

import net.decentr.module_decentr_domain.models.Profile
import net.decentr.module_decentr_db.entities.ProfileEntity

fun Profile.toEntity() = ProfileEntity(
    id = 0, address, avatar, isBanned, bio, birthDate, createdAt, emails, firstName, lastName, gender
)

fun ProfileEntity.toDomain() = Profile(
    address, avatar, isBanned, bio, birthDate, createdAt, emails, firstName, lastName, gender
)