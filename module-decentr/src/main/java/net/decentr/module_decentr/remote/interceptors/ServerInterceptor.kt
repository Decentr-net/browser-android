package net.decentr.module_decentr.remote.interceptors

import net.decentr.module_decentr.domain.provider.ServerManager
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class ServerInterceptor @Inject constructor(private val serverManager: ServerManager) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if ("localhost" == request.url.host) {
            val server = serverManager.getCurrentServer().host.split("://", limit = 2)
            val newUrl: HttpUrl = request.url.newBuilder()
                .scheme(server[0])
                .host(server[1])
                .build()
            request = request.newBuilder()
                .url(newUrl)
                .build()
        }
        return chain.proceed(request)
    }
}