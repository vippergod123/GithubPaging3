package com.example.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.data.ApiManager
import com.example.data.api.wrapApiCall
import com.example.data.repository.pagingsource.PageKeyedRemoteMediator
import com.example.data.source.local.GithubRepoDatabase
import com.example.domain.ResponseHandler
import com.example.domain.entity.GithubProfile
import com.example.domain.exception.AppException
import com.example.domain.repository.GithubRepository
import com.example.domain.usecase.FetchGithubProfileParam
import com.example.domain.usecase.GetGithubReposParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GithubRepositoryImpl(private val githubRepoDatabase: GithubRepoDatabase) : GithubRepository {
    private fun getGithubRepoApi() = ApiManager.getGithubRepoApi()
    private fun getGithubRepoDao() = githubRepoDatabase.getGithubRepoDao()

    override fun getProfile(): LiveData<GithubProfile?> = getGithubRepoDao().getProfiles().map {
        it.firstOrNull()
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getRepos(param: GetGithubReposParam) = Pager(
        config = PagingConfig(param.size, enablePlaceholders = true),
        remoteMediator = PageKeyedRemoteMediator(githubRepoDatabase, getGithubRepoApi(), param)
    ) {
        githubRepoDatabase.getGithubRepoDao().getReposPaging()
    }.flow

    override suspend fun fetchProfile(param: FetchGithubProfileParam): ResponseHandler<GithubProfile> =
        withContext(Dispatchers.IO) {
            val response = wrapApiCall {
                getGithubRepoApi().getProfile(param.repoName)
            }
            return@withContext when (response) {
                is ResponseHandler.Success -> {
                    saveProfileToLocal(response.data)
                    ResponseHandler.Success(response.data)
                }
                is ResponseHandler.Failure -> {
                    ResponseHandler.Failure(
                        response.error,
                        response.extra
                    )
                }
                else -> ResponseHandler.Failure(AppException.Failed)
            }
        }


    private suspend fun saveProfileToLocal(data: GithubProfile) = withContext(Dispatchers.IO) {
        githubRepoDatabase.getGithubRepoDao().insertProfile(data)
    }
}