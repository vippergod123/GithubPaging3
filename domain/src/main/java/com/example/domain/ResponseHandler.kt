package com.example.domain

import com.example.domain.exception.AppException

sealed class ResponseHandler<out T> {
    data class Success<out T>(val data:T) : ResponseHandler<T>()

    data class Failure(val error: Throwable = AppException.Unknown, val extra:String = ""):ResponseHandler<Nothing>()

    object Loading: ResponseHandler<Nothing>()
}