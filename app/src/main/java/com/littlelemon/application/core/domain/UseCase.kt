package com.littlelemon.application.core.domain

/**
 * UseCase interface that is implemented to create a standardized use case functionality.
 * If your use case does not need any arguments use `DefaultUseCase` instead of this.
 */
interface UseCase<in INPUT, out OUTPUT> {
    operator suspend fun invoke(input: INPUT): OUTPUT
}