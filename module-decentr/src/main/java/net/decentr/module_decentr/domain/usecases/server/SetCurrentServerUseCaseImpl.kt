package net.decentr.module_decentr.domain.usecases.server

import net.decentr.module_decentr.domain.models.Server
import net.decentr.module_decentr.domain.provider.ServerManager
import javax.inject.Inject

class SetCurrentServerUseCaseImpl @Inject constructor(private val manager: ServerManager) :
    SetCurrentServerUseCase {
    override operator fun invoke(server: Server) {
        manager.setCurrentServer(server)
    }
}