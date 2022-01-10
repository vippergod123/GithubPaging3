package com.example.data.usecase

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.FakeGithubRepoEndpoint
import com.example.data.GithubRepoFactory
import com.example.domain.repository.GithubRepository
import com.example.domain.usecase.GetGithubProfile
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
class GetGithubProfileTest {
    private lateinit var getGithubProfile: GetGithubProfile
    private lateinit var repository: GithubRepository

    @Before
    fun upset() {
        repository = mock()
        getGithubProfile = GetGithubProfile(repository)
    }

    @Test
    fun getGithubProfileWhenResponseSuccess(): Unit = runBlocking {
        val profile = GithubRepoFactory().createProfile()
        whenever(repository.getProfile()).thenReturn(MutableLiveData(profile))
        val result = getGithubProfile()
        verify(repository, times(1)).getProfile()
        verifyNoMoreInteractions(repository)
        Assert.assertEquals(result.value, profile)
    }


}