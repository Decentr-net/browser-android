package net.decentr.module_decentr.local.mapper

import net.decentr.module_decentr_db.entities.PDVEntity
import net.decentr.module_decentr_domain.models.PDV

fun PDV.toEntity(): PDVEntity {
    return when (this) {
        is net.decentr.module_decentr_domain.models.PDVHistory -> PDVEntity(0, address = address, type = type.value, domain = domain, engine = engine, query = query, timestamp = timestamp)
        is net.decentr.module_decentr_domain.models.PDVProfile -> PDVEntity(0, address = address, type = type.value, avatar = avatar, bio = bio, firstName = firstName, lastName = lastName, birthday = birthday, emails = emails, gender = gender)
    }
}

private const val DEFAULT_AVATAR = "https://public.decentr.xyz/avatars/user-avatar-1.svg"

fun PDVEntity.toDomain(): PDV {
    return when (type) {
        PDV.PDVType.TYPE_PROFILE.value -> net.decentr.module_decentr_domain.models.PDVProfile(
            address = address,
            type = PDV.PDVType.TYPE_PROFILE,
            avatar = if (avatar.isNullOrEmpty()) DEFAULT_AVATAR else avatar,
            bio = bio ?: String(),
            firstName = firstName ?: String(),
            lastName = lastName ?: String(),
            birthday = birthday ?: String(),
            emails = emails,
            gender = gender ?: String()
        )
        PDV.PDVType.TYPE_HISTORY.value -> net.decentr.module_decentr_domain.models.PDVHistory(
            address = address,
            id = id,
            type = PDV.PDVType.TYPE_HISTORY,
            domain = domain ?: String(),
            engine = engine ?: String(),
            query = query ?: String(),
            timestamp = timestamp ?: String()
        )
        else -> throw IllegalStateException("unknown pdv type")
    }
}