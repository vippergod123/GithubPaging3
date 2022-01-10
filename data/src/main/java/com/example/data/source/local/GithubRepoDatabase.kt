package com.example.data.source.local

import android.content.Context
import androidx.room.*
import androidx.room.TypeConverter
import com.example.domain.entity.GithubProfile
import com.example.domain.entity.GithubRepo
import com.example.domain.entity.RemoteKeys

@Database(
    entities = [GithubRepo::class, RemoteKeys::class,GithubProfile::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(value = [MyTypeConverter::class])
abstract class GithubRepoDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: GithubRepoDatabase? = null

        fun create(context: Context, useInMemory: Boolean): GithubRepoDatabase {
            val temp = INSTANCE
            if (temp != null) return temp
            synchronized(this) {
                val instance = if (useInMemory) {
                    Room.inMemoryDatabaseBuilder(context, GithubRepoDatabase::class.java)
                } else {
                    Room.databaseBuilder(context, GithubRepoDatabase::class.java, "github_repo.db")
                }.fallbackToDestructiveMigration() // ignore migrate
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun getGithubRepoDao(): GithubRepoDao
    abstract fun getRemoteKeysDao(): RemoteKeyDao
}