package com.example.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repos")
data class GithubRepo(

    @field:SerializedName("stargazers_count")
    val stargazersCount: Int? = null,

    @PrimaryKey
    @field:SerializedName("id")
    val id: Long,

    @field:SerializedName("forks")
    val forks: Int? = null,

    @field:SerializedName("forks_url")
    val forksUrl: String? = null,

    @field:SerializedName("full_name")
    val fullName: String? = null,

    @field:SerializedName("html_url")
    val htmlUrl: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("owner")
    val owner: Owner? = null,

    @field:SerializedName("forks_count")
    val forksCount: Int? = null,

    @field:SerializedName("watchers_count")
    val watchersCount: Int? = null

) {
    var atPage: Int = -1
}

data class Owner(

    @field:SerializedName("gists_url")
    val gistsUrl: String? = null,

    @field:SerializedName("repos_url")
    val reposUrl: String? = null,

    @field:SerializedName("following_url")
    val followingUrl: String? = null,

    @field:SerializedName("starred_url")
    val starredUrl: String? = null,

    @field:SerializedName("login")
    val login: String? = null,

    @field:SerializedName("followers_url")
    val followersUrl: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("subscriptions_url")
    val subscriptionsUrl: String? = null,

    @field:SerializedName("received_events_url")
    val receivedEventsUrl: String? = null,

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("events_url")
    val eventsUrl: String? = null,

    @field:SerializedName("html_url")
    val htmlUrl: String? = null,

    @field:SerializedName("site_admin")
    val siteAdmin: Boolean? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("gravatar_id")
    val gravatarId: String? = null,

    @field:SerializedName("node_id")
    val nodeId: String? = null,

    @field:SerializedName("organizations_url")
    val organizationsUrl: String? = null
)
