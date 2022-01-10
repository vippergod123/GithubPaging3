package com.example.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.domain.ResponseHandler
import com.example.domain.entity.GithubProfile
import com.example.domain.entity.GithubRepo
import com.example.domain.usecase.FetchGithubProfileParam
import com.example.domain.usecase.GetGithubReposParam
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun getRepos(param: GetGithubReposParam): Flow<PagingData<GithubRepo>>

    suspend fun fetchProfile(param: FetchGithubProfileParam): ResponseHandler<GithubProfile>

    fun getProfile(): LiveData<GithubProfile?>

    fun getTotalPage(): LiveData<Int?>
}