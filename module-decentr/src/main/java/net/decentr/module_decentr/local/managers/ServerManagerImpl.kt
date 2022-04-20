package net.decentr.module_decentr.local.managers

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import net.decentr.module_decentr.domain.models.Server
import net.decentr.module_decentr.domain.provider.ServerManager
import javax.inject.Inject

private const val SELECT = "Select"

class ServerManagerImpl @Inject constructor(private val context: Context) : ServerManager {

    private val servers by lazy {
        listOf(
                Server("PROD", "https://api-prod.bop.rest", "https://api-prod.bop.rest"),
                Server("DEV", "https://api-dev.bop.rest", "https://api-dev.bop.rest"),
                Server("PROD-NODE", "http://staging.olimpbattle.com", "http://staging.olimpbattle.com:8041"),
                Server("DEV-NODE", "http://dev.olimpbattle.com", "http://dev.olimpbattle.com:8041"),
                Server("QA-DEV", "http://qadev.olimpbattle.com", "http://qadev.olimpbattle.com:8041"),
                Server("STG-DEV", "http://stgdev.olimpbattle.com", "http://stgdev.olimpbattle.com:8041")
        )
    }

    @Volatile
    private var currentServer: Server? = null


    override fun getServerList(): List<Server> {
        return servers
    }

    @Synchronized
    override fun getCurrentServer(): Server {
        return currentServer ?: synchronized(this.javaClass) {
            currentServer ?: getCurrentServer(context).also {
                currentServer = it
            }
        }
    }

    override fun setCurrentServer(server: Server) {
        currentServer = server
        PreferenceManager.getDefaultSharedPreferences(context).run {
            edit(true) {
                putString(SELECT, server.name)
            }
        }
    }

    private fun getCurrentServer(context: Context): Server {
        PreferenceManager.getDefaultSharedPreferences(context).run {
            val serverName = getString(SELECT, servers[0].name) ?: servers[0].name
            return servers.firstOrNull {
                it.name == serverName
            } ?: servers.first()
        }
    }
}