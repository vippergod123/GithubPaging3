package com.example.githubsample.model

import com.example.domain.entity.GithubRepo

sealed class GithubRepoListItem(val id:Long, val atPage:Int) {
    data class Item(val repo: GithubRepo): GithubRepoListItem(repo.id, repo.atPage)
    data class Separator(val separatorID:Long, private val page:Int) : GithubRepoListItem(separatorID,page)
}