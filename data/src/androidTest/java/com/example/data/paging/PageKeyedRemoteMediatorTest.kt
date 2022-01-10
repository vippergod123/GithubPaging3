package com.example.data.paging

import android.util.Log
import androidx.paging.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.FakeGithubRepoEndpoint
import com.example.data.repository.pagingsource.PageKeyedRemoteMediator
import com.example.data.source.local.GithubRepoDatabase
import com.example.domain.entity.GithubRepo
import com.example.domain.exception.AppException
import com.example.domain.usecase.GetGithubReposParam
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PageKeyedRemoteMediatorTest {

    private val mockDb = GithubRepoDatabase.create(
        ApplicationProvider.getApplicationContext(),
        useInMemory = true
    )

    @Before
    fun setup() {
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }


    @Test
    fun refreshLoadReturnsErrorWhenHttpError() = runBlocking {
        val mockApi = FakeGithubRepoEndpoint(errorResponse = true)
        val remoteMediator = PageKeyedRemoteMediator(mockDb, mockApi, GetGithubReposParam(10, ""))
        val pagingState = PagingState<Int, GithubRepo>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result: RemoteMediator.MediatorResult =
            remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertEquals((result as RemoteMediator.MediatorResult.Error).throwable, AppException.Failed)

        val resultPrepend: RemoteMediator.MediatorResult =
            remoteMediator.load(LoadType.PREPEND, pagingState)
        assertTrue(resultPrepend is RemoteMediator.MediatorResult.Success)
        assertFalse((resultPrepend as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
//
//
        val resultAppend: RemoteMediator.MediatorResult =
            remoteMediator.load(LoadType.APPEND, pagingState)
        Log.d("DUYTS", "$resultAppend")
        assertTrue(resultAppend is RemoteMediator.MediatorResult.Success)
        assertFalse((resultAppend as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshLoadReturnsSuccessWhenResponseSuccess() = runBlocking {
        val mockApi = FakeGithubRepoEndpoint(errorResponse = false)
        val remoteMediator = PageKeyedRemoteMediator(mockDb, mockApi, GetGithubReposParam(10, ""))
        val pagingState = PagingState<Int, GithubRepo>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result: RemoteMediator.MediatorResult =
            remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)

        val resultPrepend: RemoteMediator.MediatorResult =
            remoteMediator.load(LoadType.PREPEND, pagingState)
        assertTrue(resultPrepend is RemoteMediator.MediatorResult.Success)
        assertFalse((resultPrepend as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
//
//
        val resultAppend: RemoteMediator.MediatorResult =
            remoteMediator.load(LoadType.APPEND, pagingState)
        assertTrue(resultAppend is RemoteMediator.MediatorResult.Success)
        assertFalse((resultAppend as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

}