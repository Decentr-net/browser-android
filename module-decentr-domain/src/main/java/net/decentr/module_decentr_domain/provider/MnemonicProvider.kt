package net.decentr.module_decentr_domain.provider

interface MnemonicProvider {
    fun generateSeedPhrase(): String
}