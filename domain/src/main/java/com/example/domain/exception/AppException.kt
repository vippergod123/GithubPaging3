package com.example.domain.exception

sealed class AppException : Exception(){
    object Failed: AppException()
    object InvalidParam: AppException()
    object NoNetwork: AppException()
    object UnsolvedHost: AppException()
    object NotFound: AppException()
    object Unknown: AppException()

}