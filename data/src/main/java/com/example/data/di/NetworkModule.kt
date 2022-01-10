package com.example.data.di

import com.example.data.ApiManager
import com.example.data.BuildConfig
import com.example.data.di.Constant.GITHUB_REPO_API
import com.example.data.source.GithubRepoEndpoint
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private object Constant {
    val GITHUB_DOMAIN_API = "GITHUB_DOMAIN_API"
    val GITHUB_REPO_API = "GITHUB_REPO_API"
    val CONNECTION_TIME_OUT_SECOND = 5L
}

val networkModule = module {
    single { provideGson() }
    single { provideClient() }

    factory { createService(get(named(GITHUB_REPO_API)), GithubRepoEndpoint::class.java) }

    scope<ApiManager> {
        factory {
                params -> provideRetrofit(params[0], get())
        } // Only 1 params
    }

}

inline fun <reified T> createService(retrofit: Retrofit, apiService: Class<T>): T {
    return retrofit.create(apiService)
}


fun provideRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(client)
        .build()
}


fun provideClient(): OkHttpClient {
    val builder =
        OkHttpClient.Builder().callTimeout(Constant.CONNECTION_TIME_OUT_SECOND, TimeUnit.SECONDS)
            .readTimeout(Constant.CONNECTION_TIME_OUT_SECOND, TimeUnit.SECONDS)
    if (BuildConfig.DEBUG) {
        builder.also {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            it.addInterceptor(logger)
        }
    }
    return builder.build()
}

fun provideGson(): Gson {

    return Gson()
}

fun getNetworkDi() = listOf(networkModule)