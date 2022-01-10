package com.example.domain.usecase

abstract class BaseUsecase<Param, Result> {
    abstract suspend operator fun invoke(param: Param): Result
}

abstract class BaseUsecaseNoParam<Result> {
    abstract suspend operator fun invoke(): Result
}


abstract class BaseUsecaseNoParamNoSuspend<Result> {
    abstract operator fun invoke(): Result
}