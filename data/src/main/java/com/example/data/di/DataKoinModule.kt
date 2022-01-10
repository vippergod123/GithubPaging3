package com.example.data.di

import com.example.data.repository.GithubRepositoryImpl
import com.example.data.source.local.GithubRepoDatabase
import com.example.domain.repository.GithubRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { GithubRepoDatabase.create(androidContext(), false) }
    single<GithubRepository> {GithubRepositoryImpl(get())}
}

fun getDataDi() = listOf(dataModule)