package com.example.data.usecase

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.domain.repository.GithubRepository
import com.example.domain.usecase.GetGithubReposParam
import com.example.domain.usecase.GetTotalGithubRepoPages
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class GetTotalGithubRepoPagesTest {
    private lateinit var getTotalGithubRepoPages: GetTotalGithubRepoPages
    private lateinit var repository: GithubRepository

    @Before
    fun upset() {
        repository = mock()
        getTotalGithubRepoPages = GetTotalGithubRepoPages(repository)
    }

    @Test
    fun getTotalGithubRepoPagesWhenResponseSuccess(): Unit = runBlocking {
        whenever(repository.getTotalPage()).thenReturn(MutableLiveData(1))
        val result = getTotalGithubRepoPages()
        verify(repository, times(1)).getTotalPage()
        verifyNoMoreInteractions(repository)
        Assert.assertEquals(result.value, 1)
    }


}