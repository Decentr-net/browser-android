package net.decentr.module_decentr_common.data.utils.etheriumkit

import java.math.BigInteger
import java.nio.ByteBuffer

fun String.removeLeadingZeros(): String {
    return this.trimStart('0')
}

fun ByteArray?.toRawHexString(): String {
    return this?.joinToString(separator = "") {
        it.toInt().and(0xff).toString(16).padStart(2, '0')
    } ?: ""
}

fun ByteArray?.toHexString(): String {
    val rawHex = this?.toRawHexString() ?: return ""
    return "0x$rawHex"
}

@Throws(NumberFormatException::class)
fun String.hexStringToByteArray(): ByteArray {
    return this.getByteArray()
}

@Throws(NumberFormatException::class)
fun String.hexStringToByteArrayOrNull(): ByteArray? {
    return try {
        this.getByteArray()
    } catch (error: Throwable) {
        null
    }
}

private fun String.getByteArray(): ByteArray {
    var hexWithoutPrefix = this.stripHexPrefix()
    if (hexWithoutPrefix.length % 2 == 1) {
        hexWithoutPrefix = "0$hexWithoutPrefix"
    }
    return hexWithoutPrefix.chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}

fun String.stripHexPrefix(): String {
    return if (this.startsWith("0x", true)) {
        this.substring(2)
    } else {
        this
    }
}

fun Long.toHexString(): String {
    return "0x${this.toString(16)}"
}

fun Int.toHexString(): String {
    return "0x${this.toString(16)}"
}

fun String.hexStringToLongOrNull(): Long? {
    return this.stripHexPrefix().toLongOrNull(16)
}

fun String.hexStringToIntOrNull(): Int? {
    return this.stripHexPrefix().toIntOrNull(16)
}

fun BigInteger.toHexString(): String {
    return "0x${this.toString(16)}"
}

fun String.hexStringToBigIntegerOrNull(): BigInteger? {
    return this.stripHexPrefix().toBigIntegerOrNull(16)
}

fun String.hexStringWithoutStripToBigIntegerOrNull(): BigInteger? {
    return this.toBigIntegerOrNull(16)
}

fun String.hexClearEmptyBytesEnd(): String {
    return when {
        (this.endsWith("00")) -> this.substringBeforeLast("00")
        (this.endsWith("01")) -> this.substringBeforeLast("01")
        (this.endsWith("000")) -> this.substringBeforeLast("000")
        (this.endsWith("001")) -> this.substringBeforeLast("001")
        else -> this
    }
}

fun BigInteger.toBytes(numBytes: Int): ByteArray {
    val bytes = ByteArray(numBytes)
    val biBytes = this.toByteArray()
    val start = if (biBytes.size == numBytes + 1) 1 else 0
    val length = kotlin.math.min(biBytes.size, numBytes)
    System.arraycopy(biBytes, start, bytes, numBytes - length, length)
    return bytes
}

fun Short.toBytes(): ByteArray {
    return ByteBuffer.allocate(2).putShort(this).array()
}