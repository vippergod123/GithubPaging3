package com.example.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "github_profile")
data class GithubProfile(

    @field:SerializedName("bio")
    val bio: String? = null,


    @field:SerializedName("blog")
    val blog: String? = null,

    @PrimaryKey
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("public_repos")
    val publicRepos: Int? = null,


    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("name")
    val name: String? = null,
)
