package net.decentr.module_decentr.domain.usecases.server

import net.decentr.module_decentr.domain.models.Server

interface SetCurrentServerUseCase {
    operator fun invoke(server: Server)
}