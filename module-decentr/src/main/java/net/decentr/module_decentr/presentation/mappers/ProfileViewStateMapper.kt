package net.decentr.module_decentr.presentation.mappers

import net.decentr.module_decentr.presentation.viewstates.ProfileViewState
import net.decentr.module_decentr_domain.models.Profile

fun Profile.toViewState() = ProfileViewState(address, avatar, isBanned, bio, birthDate, createdAt, emails, firstName, lastName, gender)

fun ProfileViewState.toDomain() = Profile(address, avatar, isBanned, bio, birthDate, createdAt, emails, firstName, lastName, gender)