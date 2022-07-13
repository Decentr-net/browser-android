package net.decentr.module_decentr.local.provider

import android.content.Context
import androidx.core.content.edit
import net.decentr.module_decentr_common.data.provider.KeysProvider
import javax.inject.Inject

private const val PRIV_KEY = "PRIV_KEY"
private const val PUB_KEY = "PUB_KEY"

private const val SHARED_NAME = "authorization"

class KeysProviderImpl @Inject constructor(private val context: Context) : KeysProvider {

    private var pubKey: String? = null
    private var privKey: String? = null

    private val preferences by lazy {
        context.getSharedPreferences(
            SHARED_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Synchronized
    override fun getPrivKey(): String? {
        if (privKey.isNullOrEmpty()) {
            privKey = preferences.run {
                getString(PRIV_KEY, null)
            }
        }
        return privKey
    }

    @Synchronized
    override fun getPubKey(): String? {
        if (pubKey.isNullOrEmpty()) {
            pubKey = preferences.run {
                getString(PUB_KEY, null)
            }
        }
        return pubKey
    }

    override fun updateKeys(privKey: String, pubKey: String) {
        this.privKey = privKey
        this.pubKey = pubKey
        preferences.edit(true) {
            putString(PRIV_KEY, privKey)
            putString(PUB_KEY, pubKey)
        }
    }

    override fun skipKeys() {
        this.pubKey = null
        this.privKey = null
        preferences.edit(true) {
            putString(PRIV_KEY, null)
            putString(PUB_KEY, null)
        }
    }
}