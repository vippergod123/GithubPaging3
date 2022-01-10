package com.example.data.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.FakeGithubRepoEndpoint
import com.example.data.collectDataForTest
import com.example.data.repository.pagingsource.PageKeyedRemoteMediator
import com.example.data.source.GithubRepoEndpoint
import com.example.data.source.local.GithubRepoDatabase
import com.example.domain.entity.GithubRepo
import com.example.domain.repository.GithubRepository
import com.example.domain.usecase.GetGithubRepos
import com.example.domain.usecase.GetGithubReposParam
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class GetGithubRepoTest {
    private val param = GetGithubReposParam(10, "")
    private lateinit var getGithubRepos: GetGithubRepos
    private val mockDb = GithubRepoDatabase.create(
        ApplicationProvider.getApplicationContext(),
        useInMemory = true
    )

    private lateinit var repository: GithubRepository

    @Before
    fun upset() {
        repository = mock()
        getGithubRepos = GetGithubRepos(repository)
    }

    @Test
    fun getGitHubReposWhenResponseSuccess(): Unit = runBlocking {
        val api = FakeGithubRepoEndpoint(errorResponse = false)
        whenever(repository.getRepos(param)).thenReturn(mockPager(api))
        val result = getGithubRepos(param)
        verify(repository, times(1)).getRepos(param)
        verifyNoMoreInteractions(repository)

        result.take(1).collect {
            it.collectDataForTest()
            Assert.assertEquals(mockDb.getGithubRepoDao().getRepoSize(), api.getFakeRepos().size)
        }
    }

    @Test
    fun getGitHubReposWhenResponseError(): Unit = runBlocking {
        val api = FakeGithubRepoEndpoint(errorResponse = true)
        whenever(repository.getRepos(param)).thenReturn(mockPager(api))
        val result: Flow<PagingData<GithubRepo>> = getGithubRepos(param)
        verify(repository, times(1)).getRepos(param)
        verifyNoMoreInteractions(repository)
        result.take(1).collect {
            it.collectDataForTest()
            Assert.assertEquals(mockDb.getGithubRepoDao().getRepoSize(),0)
        }
    }


    @OptIn(ExperimentalPagingApi::class)
    private fun mockPager(api: GithubRepoEndpoint): Flow<PagingData<GithubRepo>> {
        return Pager(
            config = PagingConfig(param.size, enablePlaceholders = true),
            remoteMediator = PageKeyedRemoteMediator(mockDb, api, param)
        ) {
            mockDb.getGithubRepoDao().getReposPaging()
        }.flow
    }
}