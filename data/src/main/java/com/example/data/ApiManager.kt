package com.example.data

import com.example.base_lib.ext.scope
import com.example.data.source.GithubRepoEndpoint
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import retrofit2.Retrofit

object ApiManager: KoinComponent {
    private const val finalDomain = "https://api.github.com/"
    fun getGithubRepoApi(): GithubRepoEndpoint {
        return createApi(clazz = GithubRepoEndpoint::class.java)
    }

    private fun <T> createApi(clazz: Class<T>): T {
        val retrofit = scope.get<Retrofit> {
            parametersOf(finalDomain)
        }
        return retrofit.create(clazz)
    }
}