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
class FetchGithubProfileTest {

}