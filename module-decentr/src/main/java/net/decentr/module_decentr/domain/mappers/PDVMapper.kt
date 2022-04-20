package net.decentr.module_decentr.domain.mappers

import net.decentr.module_decentr.domain.models.PDV
import net.decentr.module_decentr.domain.models.PDVHistory
import net.decentr.module_decentr.domain.models.PDVProfile
import net.decentr.module_decentr.remote.model.PDVResponse
import java.text.SimpleDateFormat
import java.util.*

private const val DEFAULT_AVATAR = "https://public.decentr.xyz/avatars/user-avatar-1.svg"

fun PDV.toRemote(): PDVResponse {
    return when (this) {
        is PDVHistory -> PDVResponse(
            type = PDV.PDVType.TYPE_HISTORY.value,
            domain = domain,
            engine = engine,
            query = query,
            timestamp = timestamp
        )
        is PDVProfile -> PDVResponse(
            type = PDV.PDVType.TYPE_PROFILE.value,
            avatar = if (avatar.isNullOrEmpty()) DEFAULT_AVATAR else avatar,
            bio = bio,
            birthday = birthday,
            emails = emails,
            firstName = firstName,
            lastName = lastName,
            gender = gender
        )
        else -> throw IllegalStateException("This PDV type not imported yet")
    }
}

//fun PDVResponse.toDomain(): PDV {
//    return when (type) {
//        PDV.PDVType.TYPE_HISTORY.value -> {
//            PDVHistory(
//                type = PDV.PDVType.TYPE_HISTORY,
//                domain = domain ?: String(),
//                timestamp = timestamp ?: String(),
//                engine = engine ?: String(),
//                query = query ?: String()
//            )
//        }
//        else -> throw IllegalStateException("This PDV type not imported yet")
//    }
//}