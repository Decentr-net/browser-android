package net.decentr.module_decentr.domain.provider

interface MnemonicProvider {
    fun generateSeedPhrase(): String
}