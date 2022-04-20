package net.decentr.module_decentr.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class DefaultInterceptor(private val versionName: String,
                         private val versionCode: Long,
                         private val platformName: String = "android") : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("app-version", versionName)
            .addHeader("app-version-code", versionCode.toString())
            .addHeader("platform", platformName)
            .addHeader("locale", Locale.getDefault().language)
            .method(original.method, original.body)
        return chain.proceed(requestBuilder.build())
    }
}