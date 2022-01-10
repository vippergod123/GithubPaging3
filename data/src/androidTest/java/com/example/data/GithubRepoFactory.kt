package com.example.data

import com.example.domain.entity.GithubRepo
import com.example.domain.entity.Owner
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class GithubRepoFactory {
    private val counter = AtomicInteger(0)
    fun createGithubRepo(): GithubRepo {
        val id = counter.incrementAndGet()
        val randomInt = Random.nextInt(100)
        return GithubRepo(
            id = id.toLong(),
            stargazersCount = randomInt,
            forks = randomInt,
            forksCount = randomInt,
            forksUrl = "for-url",
            fullName = "full-name",
            htmlUrl = "html-url",
            description = "description",
            owner = Owner(avatarUrl = "avatar-url"),
            watchersCount = randomInt
        )
    }

    fun listOfFakeRepos() = listOf(
        createGithubRepo(),
        createGithubRepo(),
        createGithubRepo(),
        createGithubRepo(),
    )
}