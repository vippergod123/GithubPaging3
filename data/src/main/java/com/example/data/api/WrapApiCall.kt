package com.example.data.api

import android.util.Log
import com.example.domain.ResponseHandler
import com.example.domain.exception.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

suspend inline fun <reified T> wrapApiCall(
    crossinline api: suspend () -> Response<T>
): ResponseHandler<T> {

    return try {
        val response = withContext(Dispatchers.IO) {
            api()
        }
        when (response.code()) {
            200 -> {
                ResponseHandler.Success(response.body()!!)
            }
            else -> {
                ResponseHandler.Failure(AppException.Failed)
            }
        }
    } catch (ex: Exception) {
        return when (ex) {
            is HttpException, AppException.NoNetwork -> {
                ResponseHandler.Failure(AppException.NoNetwork)
            }
            is UnknownHostException -> {
                ResponseHandler.Failure(AppException.UnsolvedHost)
            }
            is IllegalArgumentException-> {
                ResponseHandler.Failure(AppException.Failed)
            }
            else -> {
                ResponseHandler.Failure(ex)
            }
        }
    }
}