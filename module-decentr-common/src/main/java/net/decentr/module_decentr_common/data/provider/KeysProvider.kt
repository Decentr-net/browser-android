package net.decentr.module_decentr_common.data.provider

interface KeysProvider {

    fun getPubKey(): String?
    fun getPrivKey(): String?

    fun updateKeys(privKey: String, pubKey: String)

    fun skipKeys()
}