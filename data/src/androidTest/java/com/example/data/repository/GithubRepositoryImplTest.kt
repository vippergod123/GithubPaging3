package com.example.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.FakeGithubRepoEndpoint
import com.example.data.source.local.GithubRepoDatabase
import com.example.domain.usecase.GetGithubReposParam
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class GithubRepositoryImplTest {
    private val mockDb = GithubRepoDatabase.create(
        ApplicationProvider.getApplicationContext(),
        useInMemory = true
    )
    private val githubRepoDatabase = GithubRepositoryImpl(mockDb)
    @Test
    fun getRepos() {
        githubRepoDatabase.getRepos(GetGithubReposParam(10, ""))

    }
}