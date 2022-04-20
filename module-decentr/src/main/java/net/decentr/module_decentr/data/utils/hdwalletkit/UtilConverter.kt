package net.decentr.module_decentr.data.utils.hdwalletkit

import okhttp3.internal.and
import org.bouncycastle.crypto.digests.RIPEMD160Digest
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object UtilConverter {

    private const val PREFIX_SALT = "decentr"

    fun toBigInt(arr: ByteArray): BigInteger {
        val rev = ByteArray(arr.size + 1)
        var i = 0
        var j = arr.size
        while (j > 0) {
            rev[j] = arr[i]
            i++
            j--
        }
        return BigInteger(rev)
    }

    fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] =
                ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02x", b and 0xff))
        }
        return sb.toString()
    }

    fun getAddress(pub: ByteArray): String {
        var result = String()
        val digest: MessageDigest = getSha256Digest()
        val hash = digest.digest(pub)
        val digest2 = RIPEMD160Digest()
        digest2.update(hash, 0, hash.size)
        val hash3 = ByteArray(digest2.digestSize)
        digest2.doFinal(hash3, 0)
        return try {
            val converted: ByteArray? =
                convertBits(hash3, 8, 5, true)
            result = converted?.let {
                bech32Encode(
                    PREFIX_SALT.toByteArray(),
                    it
                )
            }.toString()
            result
        } catch (e: Exception) {
            ""
        }
    }

    @Throws(java.lang.Exception::class)
    private fun convertBits(data: ByteArray, frombits: Int, tobits: Int, pad: Boolean): ByteArray? {
        var acc = 0
        var bits = 0
        val baos = ByteArrayOutputStream()
        val maxv = (1 shl tobits) - 1
        for (i in data.indices) {
            val value: Int = data[i] and 0xff
            if (value ushr frombits != 0) {
                throw java.lang.Exception("invalid data range: data[$i]=$value (frombits=$frombits)")
            }
            acc = acc shl frombits or value
            bits += frombits
            while (bits >= tobits) {
                bits -= tobits
                baos.write(acc ushr bits and maxv)
            }
        }
        if (pad) {
            if (bits > 0) {
                baos.write(acc shl tobits - bits and maxv)
            }
        } else if (bits >= frombits) {
            throw java.lang.Exception("illegal zero padding")
        } else if (acc shl tobits - bits and maxv != 0) {
            throw java.lang.Exception("non-zero padding")
        }
        return baos.toByteArray()
    }

    private val CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l"

    private fun bech32Encode(hrp: ByteArray, data: ByteArray): String? {
        val chk: ByteArray = createChecksum(hrp, data)
        val combined = ByteArray(chk.size + data.size)
        System.arraycopy(data, 0, combined, 0, data.size)
        System.arraycopy(chk, 0, combined, data.size, chk.size)
        val xlat = ByteArray(combined.size)
        for (i in combined.indices) {
            xlat[i] = CHARSET.get(combined[i].toInt()).toByte()
        }
        val ret = ByteArray(hrp.size + xlat.size + 1)
        System.arraycopy(hrp, 0, ret, 0, hrp.size)
        System.arraycopy(byteArrayOf(0x31), 0, ret, hrp.size, 1)
        System.arraycopy(xlat, 0, ret, hrp.size + 1, xlat.size)
        return String(ret)
    }

    private fun createChecksum(hrp: ByteArray, data: ByteArray): ByteArray {
        val zeroes = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        val expanded: ByteArray = hrpExpand(hrp)
        val values = ByteArray(zeroes.size + expanded.size + data.size)
        System.arraycopy(expanded, 0, values, 0, expanded.size)
        System.arraycopy(data, 0, values, expanded.size, data.size)
        System.arraycopy(zeroes, 0, values, expanded.size + data.size, zeroes.size)
        val polymod: Int = polymod(values) xor 1
        val ret = ByteArray(6)
        for (i in ret.indices) {
            ret[i] = (polymod shr 5 * (5 - i) and 0x1f).toByte()
        }
        return ret
    }

    private fun hrpExpand(hrp: ByteArray): ByteArray {
        val buf1 = ByteArray(hrp.size)
        val buf2 = ByteArray(hrp.size)
        val mid = ByteArray(1)
        for (i in hrp.indices) {
            buf1[i] = ((hrp[i].toInt()) shr 5).toByte()
        }
        mid[0] = 0x00
        for (i in hrp.indices) {
            buf2[i] = (hrp[i] and 0x1f).toByte()
        }
        val ret = ByteArray(hrp.size * 2 + 1)
        System.arraycopy(buf1, 0, ret, 0, buf1.size)
        System.arraycopy(mid, 0, ret, buf1.size, mid.size)
        System.arraycopy(buf2, 0, ret, buf1.size + mid.size, buf2.size)
        return ret
    }

    private fun polymod(values: ByteArray): Int {
        val GENERATORS = intArrayOf(0x3b6a57b2, 0x26508e6d, 0x1ea119fa, 0x3d4233dd, 0x2a1462b3)
        var chk = 1
        for (b in values) {
            val top = (chk shr 0x19).toByte()
            chk = (b.toInt() xor (chk and 0x1ffffff shl 5))
            for (i in 0..4) {
                chk = chk xor if (top.toInt() shr i and 1 == 1) GENERATORS[i] else 0
            }
        }
        return chk
    }


    private fun getSha256Digest(): MessageDigest {
        return try {
            MessageDigest.getInstance("SHA-256")
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e) //cannot happen
        }
    }
}