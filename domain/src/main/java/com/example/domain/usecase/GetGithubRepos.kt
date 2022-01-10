package com.example.domain.usecase

import androidx.paging.PagingData
import com.example.domain.entity.GithubRepo
import com.example.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow

open class GetGithubRepos(private val githubRepository: GithubRepository) :
    BaseUsecase<GetGithubReposParam, Flow<PagingData<GithubRepo>>>() {
    override suspend fun invoke(param: GetGithubReposParam): Flow<PagingData<GithubRepo>> {
        return githubRepository.getRepos(param)
    }
}

data class GetGithubReposParam(
    val size: Int,
    val repoName:String,
)