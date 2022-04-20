package net.decentr.module_decentr.data.utils

import android.util.Base64
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESHelper {
    private const val KEY_SIZE = 256
    private const val IV_SIZE = 128
    private const val HASH_CIPHER = "AES/CBC/PKCS7Padding"
    private const val AES = "AES"
    private const val KDF_DIGEST = "MD5"

    @Throws(GeneralSecurityException::class)
    fun decryptCryptoJS(password: String, cipherText: String): String {


        val ctBytes: ByteArray =
            Base64.decode(cipherText.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
        val saltBytes: ByteArray = ctBytes.copyOfRange(8, 16)
        val ciphertextBytes: ByteArray = ctBytes.copyOfRange(16, ctBytes.size)
        val key = ByteArray(KEY_SIZE / 8)
        val iv = ByteArray(IV_SIZE / 8)

        EvpKDF(password.toByteArray(Charsets.UTF_8), KEY_SIZE, IV_SIZE, saltBytes, key, iv)

        val cipher = Cipher.getInstance(HASH_CIPHER)
        val keyS: SecretKey = SecretKeySpec(key, AES)

        cipher.init(Cipher.DECRYPT_MODE, keyS, IvParameterSpec(iv))
        val plainText = cipher.doFinal(ciphertextBytes)
        return String(plainText)
    }

    @Suppress("SameParameterValue")
    @Throws(NoSuchAlgorithmException::class)
    private fun EvpKDF(
        password: ByteArray,
        keySize: Int,
        ivSize: Int,
        salt: ByteArray,
        resultKey: ByteArray,
        resultIv: ByteArray
    ): ByteArray {
        return EvpKDF(password, keySize, ivSize, salt, 1, KDF_DIGEST, resultKey, resultIv)
    }

    @Suppress("SameParameterValue")
    @Throws(NoSuchAlgorithmException::class)
    private fun EvpKDF(
        password: ByteArray,
        keySizeParam: Int,
        ivSizeParam: Int,
        salt: ByteArray,
        iterations: Int,
        hashAlgorithm: String,
        resultKey: ByteArray,
        resultIv: ByteArray
    ): ByteArray {
        var keySize = keySizeParam
        var ivSize = ivSizeParam
        keySize /= 32
        ivSize /= 32
        val targetKeySize = keySize + ivSize
        val derivedBytes = ByteArray(targetKeySize * 4)
        var numberOfDerivedWords = 0
        var block: ByteArray? = null
        val hasher: MessageDigest = MessageDigest.getInstance(hashAlgorithm)
        while (numberOfDerivedWords < targetKeySize) {
            if (block != null) {
                hasher.update(block)
            }
            hasher.update(password)
            block = hasher.digest(salt)
            hasher.reset()

            // Iterations
            for (i in 1 until iterations) {
                block = hasher.digest(block)
                hasher.reset()
            }
            System.arraycopy(
                block, 0, derivedBytes, numberOfDerivedWords * 4,
                (block?.size ?: 0).coerceAtMost((targetKeySize - numberOfDerivedWords) * 4)
            )
            numberOfDerivedWords += (block?.size ?: 1) / 4

        }
        System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4)
        System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4)
        return derivedBytes // key + iv
    }
}

