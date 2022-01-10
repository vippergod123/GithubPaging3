package com.example.domain.di

import com.example.domain.usecase.FetchGithubProfile
import com.example.domain.usecase.GetGithubProfile
import com.example.domain.usecase.GetGithubRepos
import org.koin.core.module.Module
import org.koin.dsl.module

val domainModule = module {
    single { GetGithubRepos(get()) }
    single { FetchGithubProfile(get()) }
    single { GetGithubProfile(get()) }

}

fun getDomainDi(): List<Module> = listOf(domainModule)