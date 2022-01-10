package com.example.data

import com.example.data.source.GithubRepoEndpoint
import com.example.domain.entity.GithubRepo
import com.example.domain.exception.AppException
import okhttp3.ResponseBody
import retrofit2.Response

class FakeGithubRepoEndpoint(private val errorResponse:Boolean = false):GithubRepoEndpoint {
    private val githubRepoFactory = GithubRepoFactory()
    private val fakeRepos = listOf<GithubRepo>(
        githubRepoFactory.createGithubRepo(),
        githubRepoFactory.createGithubRepo(),
        githubRepoFactory.createGithubRepo(),
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

    fun getFakeRepos() = fakeRepos
}