package com.example.domain.usecase

import androidx.lifecycle.LiveData
import com.example.domain.entity.GithubProfile
import com.example.domain.repository.GithubRepository

open class GetGithubProfile(private val githubRepository: GithubRepository) :
    BaseUsecaseNoParamNoSuspend<LiveData<GithubProfile?>>() {
    override fun invoke(): LiveData<GithubProfile?> {
        return githubRepository.getProfile()
    }
}

