package com.example.githubsample.di

import com.example.githubsample.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appKoin = module {
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
}