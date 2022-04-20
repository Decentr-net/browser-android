package net.decentr.module_decentr.remote

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import net.decentr.module_decentr.BuildConfig
import net.decentr.module_decentr.data.provider.KeysProvider
import net.decentr.module_decentr.di.qualifier.CerberusApiClient
import net.decentr.module_decentr.di.qualifier.DecentrApiClient
import net.decentr.module_decentr.di.qualifier.VulcanApiClient
import net.decentr.module_decentr.remote.interceptors.DefaultInterceptor
import net.decentr.module_decentr.remote.interceptors.ServerInterceptor
import net.decentr.module_decentr.remote.interceptors.SignatureInterceptor
import net.decentr.module_decentr.remote.services.CerberusService
import net.decentr.module_decentr.remote.services.DecentrService
import net.decentr.module_decentr.remote.services.VulcanService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object RemoteModule {
    private const val NETWORK_TIMEOUT = 30L // Second

    @Provides
    @Singleton
    fun providesDefaultInterceptor(context: Context): DefaultInterceptor {
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        val version = info?.versionName ?: ""
        val code = PackageInfoCompat.getLongVersionCode(info)
        return DefaultInterceptor(version, code, "android")
    }

    @Provides
    @Singleton
    fun providesSignatureInterceptor(keysProvider: KeysProvider): SignatureInterceptor {
        return SignatureInterceptor(keysProvider)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson().newBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Provides
    @Singleton
    fun providesMoshiConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    @CerberusApiClient
    fun providesCerberusRetrofit(
        @CerberusApiClient okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://cerberus.mainnet.decentr.xyz")
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    @VulcanApiClient
    fun providesVulcanRetrofit(
        @VulcanApiClient okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://vulcan.mainnet.decentr.xyz")
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    @DecentrApiClient
    fun providesDecentrRetrofit(
        @DecentrApiClient okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://rest.mainnet.decentr.xyz")
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    @CerberusApiClient
    fun providesCerberusApiOkHttpClient(
        signatureInterceptor: SignatureInterceptor,
        serverInterceptor: ServerInterceptor,
        defaultInterceptor: DefaultInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }

        okHttpClientBuilder
            .addInterceptor(signatureInterceptor)
            .addInterceptor(serverInterceptor)
            .addInterceptor(defaultInterceptor)
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    @VulcanApiClient
    fun providesVulcanApiOkHttpClient(
        serverInterceptor: ServerInterceptor,
        defaultInterceptor: DefaultInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }

        okHttpClientBuilder
            .addInterceptor(serverInterceptor)
            .addInterceptor(defaultInterceptor)
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    @DecentrApiClient
    fun providesDecentrApiOkHttpClient(
        serverInterceptor: ServerInterceptor,
        defaultInterceptor: DefaultInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }

        okHttpClientBuilder
            .addInterceptor(serverInterceptor)
            .addInterceptor(defaultInterceptor)
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun providesCerberusService(@CerberusApiClient retrofit: Retrofit): CerberusService {
        return retrofit.create(CerberusService::class.java)
    }

    @Provides
    @Singleton
    fun provideDecentrService(@DecentrApiClient retrofit: Retrofit): DecentrService {
        return retrofit.create(DecentrService::class.java)
    }

    @Provides
    @Singleton
    fun provideVulcanService(@VulcanApiClient retrofit: Retrofit): VulcanService {
        return retrofit.create(VulcanService::class.java)
    }


}