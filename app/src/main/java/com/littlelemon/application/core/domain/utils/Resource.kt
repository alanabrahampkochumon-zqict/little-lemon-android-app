package com.littlelemon.application.core.domain.utils


/**
 * A Simple Wrapper Class for Handling Network Error States
 */
sealed interface Resource<out T> {

    data class Loading<T>(val data: T? = null) : Resource<T>

    data class Success<T>(val data: T? = null) : Resource<T>

    data class Failure<T>(
        val data: T? = null,
        val errorMessage: String? = null,
        val error: Error? = null
    ) :
        Resource<T>
}