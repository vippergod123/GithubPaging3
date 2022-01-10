package com.example.data

import com.example.data.source.GithubRepoEndpoint
import com.example.domain.entity.GithubProfile
import com.example.domain.entity.GithubRepo
import retrofit2.Response

class FakeGithubRepoEndpoint(private val errorResponse: Boolean = false) : GithubRepoEndpoint {
    private val githubRepoFactory = GithubRepoFactory()
    private val fakeRepos = listOf<GithubRepo>(
        githubRepoFactory.createGithubRepo(),
        githubRepoFactory.createGithubRepo(),
        githubRepoFactory.createGithubRepo(),
    )

    private val fakeProfile = listOf(
        githubRepoFactory.createProfile(),
    )

    override suspend fun fetchRepos(
        repoName: String,
        pageSize: Int,
        page: Int
    ): Response<List<GithubRepo>> {
        if (errorResponse) {
            return Response.success(-200, listOf())
        }
        return Response.success(fakeRepos)
    }

    override suspend fun getProfile(repoName: String): Response<GithubProfile> {
        if (errorResponse) {
            val profile: GithubProfile? = null
            return Response.success(-200, profile)
        }
        return Response.success(fakeProfile.first())
    }

    fun getFakeRepos() = fakeRepos
}