package net.decentr.module_decentr_domain.models

data class Server(
        val name: String,
        val host: String,
        val socket: String? = null
)