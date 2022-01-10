package com.example.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entity.GithubProfile
import com.example.domain.entity.GithubRepo


@Dao
interface GithubRepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile:GithubProfile)

    @Query("SELECT * FROM github_profile")
    fun getProfiles(): LiveData<List<GithubProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repo: List<GithubRepo>)

    @Query("SELECT * FROM repos ORDER BY atPage ASC")
    fun getReposPaging(): PagingSource<Int, GithubRepo>

    @Query("DELETE FROM repos")
    suspend fun clearRepos()

    @Query("SELECT COUNT(*) FROM repos")
    suspend fun getRepoSize(): Int

}
