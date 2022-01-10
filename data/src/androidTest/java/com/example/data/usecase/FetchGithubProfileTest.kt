package com.example.data.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.GithubRepoFactory
import com.example.domain.ResponseHandler
import com.example.domain.exception.AppException
import com.example.domain.repository.GithubRepository
import com.example.domain.usecase.FetchGithubProfile
import com.example.domain.usecase.FetchGithubProfileParam
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
class FetchGithubProfileTest {
    private val param = FetchGithubProfileParam("")

    private lateinit var fetchGithubProfile: FetchGithubProfile
    private lateinit var repository: GithubRepository

    @Before
    fun upset() {
        repository = mock()
        fetchGithubProfile = FetchGithubProfile(repository)
    }

    @Test
    fun fetchGithubProfileWhenResponseSuccess(): Unit = runBlocking {
        val profile = GithubRepoFactory().createProfile()
        whenever(repository.fetchProfile(param)).thenReturn(ResponseHandler.Success(profile))
        val result = fetchGithubProfile(param)
        verify(repository, times(1)).fetchProfile(param)
        verifyNoMoreInteractions(repository)

        when (result) {
            is ResponseHandler.Success -> {
                Assert.assertEquals(profile, result.data)
            }
            else -> {
                Assert.fail("Only success in this test case")
            }
        }
    }

    @Test
    fun fetchGithubProfileWhenResponseError(): Unit = runBlocking {
        val profile = AppException.Failed
        whenever(repository.fetchProfile(param)).thenReturn(ResponseHandler.Failure(profile))
        val result = fetchGithubProfile(param)
        verify(repository, times(1)).fetchProfile(param)
        verifyNoMoreInteractions(repository)

        when (result) {
            is ResponseHandler.Failure -> {
                Assert.assertEquals(profile, result.error)
            }
            else -> {
                Assert.fail("Only error in this test case")
            }
        }
    }


}