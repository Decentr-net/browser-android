package net.decentr.module_decentr.remote.interceptors

import net.decentr.module_decentr_common.data.utils.etheriumkit.CryptoUtils
import net.decentr.module_decentr_common.data.utils.etheriumkit.hexClearEmptyBytesEnd
import net.decentr.module_decentr_common.data.utils.etheriumkit.hexStringWithoutStripToBigIntegerOrNull
import net.decentr.module_decentr_common.data.utils.etheriumkit.toRawHexString
import net.decentr.module_decentr_common.data.utils.hdwalletkit.toSha256
import net.decentr.module_decentr_common.data.provider.KeysProvider
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject

class SignatureInterceptor @Inject constructor(private val keysProvider: KeysProvider) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val privKey = keysProvider.getPrivKey()
        val pubKey = keysProvider.getPubKey()
        return if (privKey != null && pubKey != null) {
            val string = bodyToString(chain.request()) + chain.request().url.toUri().path
            val digestForSignature = string.toByteArray(Charset.defaultCharset())
            val signature = CryptoUtils.ellipticSign(digestForSignature.toSha256(), privKey.hexStringWithoutStripToBigIntegerOrNull()!!).toRawHexString()

            val requestBuilder = original.newBuilder()
                .addHeader("Signature", signature.hexClearEmptyBytesEnd())
                .addHeader("Public-Key", pubKey)
                .method(original.method, original.body)
            chain.proceed(requestBuilder.build())
        } else {
            chain.proceed(original)
        }
    }

    private fun bodyToString(request: Request): String? {
        return try {
            val copy: Request = request.newBuilder().build()
            val buffer = okio.Buffer()
            copy.body?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }

}