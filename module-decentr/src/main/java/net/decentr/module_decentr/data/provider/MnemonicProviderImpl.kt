package net.decentr.module_decentr.data.provider

import net.decentr.module_decentr.data.utils.hdwalletkit.Mnemonic
import net.decentr.module_decentr.domain.provider.MnemonicProvider
import javax.inject.Inject

class MnemonicProviderImpl @Inject constructor(): MnemonicProvider {

//    https://github.com/zcash/kotlin-bip39

    override fun generateSeedPhrase(): String {
        val mnemonic = Mnemonic().generate(Mnemonic.Strength.VeryHigh)
        var string = String()
        mnemonic.forEach {
            string += " $it"
        }
        return string.trim()
    }
}