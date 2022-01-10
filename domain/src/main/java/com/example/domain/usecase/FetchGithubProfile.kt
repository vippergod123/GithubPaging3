package com.example.domain.usecase

import com.example.domain.ResponseHandler
import com.example.domain.entity.GithubProfile
import com.example.domain.repository.GithubRepository


open class FetchGithubProfile(private val githubRepository: GithubRepository) :
    BaseUsecase<FetchGithubProfileParam, ResponseHandler<GithubProfile>>() {
    override suspend fun invoke(param: FetchGithubProfileParam): ResponseHandler<GithubProfile> {
        return githubRepository.fetchProfile(param)
    }
}

data class FetchGithubProfileParam(
    val repoName: String,
)
