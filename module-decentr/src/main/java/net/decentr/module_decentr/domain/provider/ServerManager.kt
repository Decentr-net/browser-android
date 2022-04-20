package net.decentr.module_decentr.domain.provider

import net.decentr.module_decentr.domain.models.Server

interface ServerManager {
    fun getServerList(): List<Server>
    fun getCurrentServer(): Server
    fun setCurrentServer(server: Server)
}