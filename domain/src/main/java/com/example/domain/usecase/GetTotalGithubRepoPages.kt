package com.example.domain.usecase

import androidx.lifecycle.LiveData
import com.example.domain.repository.GithubRepository


open class GetTotalGithubRepoPages(private val githubRepository: GithubRepository) :
    BaseUsecaseNoParamNoSuspend<LiveData<Int?>>() {
    override  fun invoke(): LiveData<Int?> {
        return githubRepository.getTotalPage()
    }
}