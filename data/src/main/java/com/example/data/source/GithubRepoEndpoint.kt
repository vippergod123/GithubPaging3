package com.example.data.source

import com.example.domain.entity.GithubProfile
import com.example.domain.entity.GithubRepo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubRepoEndpoint {
    @Headers("Authorization: 1230f06b1082398730f9")
    @GET("users/{repo}/repos")
    suspend fun fetchRepos(
        @Path("repo") repoName:String = "google",
        @Query("per_page") pageSize:Int,
        @Query("page") page:Int,
    ): Response<List<GithubRepo>>

    @GET("users/{repo}")
    suspend fun getProfile(
        @Path("repo") repoName:String = "google",
    ): Response<GithubProfile>
}