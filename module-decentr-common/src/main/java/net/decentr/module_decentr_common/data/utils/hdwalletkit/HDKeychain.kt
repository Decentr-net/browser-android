package net.decentr.module_decentr_common.data.utils.hdwalletkit

/*
  Copyright 2013-2014 Ronald W Hoffman
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

class HDKeychain(seed: ByteArray, private val compressed: Boolean = true) {

    private var privateKey: HDKey = HDKeyDerivation.createRootKey(seed)

    /// Parses the BIP32 path and derives the chain of keychains accordingly.
    /// Path syntax: (m?/)?([0-9]+'?(/[0-9]+'?)*)?
    /// The following paths are valid:
    ///
    /// "" (root key)
    /// "m" (root key)
    /// "/" (root key)
    /// "m/0'" (hardened child #0 of the root key)
    /// "/0'" (hardened child #0 of the root key)
    /// "0'" (hardened child #0 of the root key)
    /// "m/44'/1'/2'" (BIP44 testnet account #2)
    /// "/44'/1'/2'" (BIP44 testnet account #2)
    /// "44'/1'/2'" (BIP44 testnet account #2)
    ///
    /// The following paths are invalid:
    ///
    /// "m / 0 / 1" (contains spaces)
    /// "m/b/c" (alphabetical characters instead of numerical indexes)
    /// "m/1.2^3" (contains illegal characters)
    fun getKeyByPath(path: String): HDKey {
        var key = privateKey

        var derivePath = path
        if (derivePath == "m" || derivePath == "/" || derivePath == "") {
            return key
        }
        if (derivePath.contains("m/")) {
            derivePath = derivePath.drop(2)
        }
        for (chunk in derivePath.split("/")) {
            var hardened = false
            var indexText: String = chunk
            if (chunk.contains("'")) {
                hardened = true
                indexText = indexText.dropLast(1)
            }
            val index = indexText.toInt()
            key = HDKeyDerivation.deriveChildKey(key, index, hardened)
        }

        return key
    }

    fun deriveNonHardenedChildKeys(parentPrivateKey: HDKey, indices: IntRange): List<HDKey> {
        val keys = mutableListOf<HDKey>()
        for (index in indices) {
            val childHDKey = HDKeyDerivation.deriveChildKey(parentPrivateKey, index, false)
            keys.add(childHDKey)
        }
        return keys
    }

}
