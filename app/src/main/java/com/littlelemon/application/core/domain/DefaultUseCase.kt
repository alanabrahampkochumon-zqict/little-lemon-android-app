package com.littlelemon.application.core.domain

/**
 * UseCase interface that is implemented when your invocation function does not take any argument.
 * If your use case takes argument, then it is recommended to use `UseCase`.
 */
interface DefaultUseCase<out OUTPUT> {
    suspend operator fun invoke(): OUTPUT
}