package net.decentr.module_decentr.data.provider

interface KeysProvider {

    fun getPubKey(): String?
    fun getPrivKey(): String?

    fun updateKeys(privKey: String, pubKey: String)

    fun skipKeys()
}