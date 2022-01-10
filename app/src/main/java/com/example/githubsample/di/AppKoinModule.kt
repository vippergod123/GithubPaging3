package com.example.githubsample.di

import com.example.data.di.getDataDi
import com.example.data.di.getNetworkDi
import com.example.domain.di.getDomainDi
import org.koin.core.module.Module

fun getDi(): List<Module> = mutableListOf(appKoin).apply {
    addAll(getDataDi())
    addAll(getNetworkDi())
    addAll(getDomainDi())
}