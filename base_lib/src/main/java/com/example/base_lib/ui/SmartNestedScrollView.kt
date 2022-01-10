package com.example.base_lib.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import timber.log.Timber

abstract class ScrollView : ViewModel() {
    private val handleExp = CoroutineExceptionHandler { _, ex ->
        Timber.e("Failed ${ex.message}")
    }

    protected val uiScope = CoroutineScope(SupervisorJob() + Dispatchers.Main + handleExp)
    protected val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExp)

    override fun onCleared() {
        super.onCleared()
        if (uiScope.isActive) uiScope.coroutineContext.cancelChildren()
        if (ioScope.isActive) ioScope.coroutineContext.cancelChildren()
    }

}