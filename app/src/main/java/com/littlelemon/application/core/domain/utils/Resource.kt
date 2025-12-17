package com.littlelemon.application.core.domain.utils


/**
 * A Simple Wrapper Class for Handling Network Error States
 */
sealed interface Resource<out T> {

    object Loading : Resource<Nothing>

    data class Success<T>(val data: T? = null) : Resource<T>

    data class Failure(val errorMessage: String? = null, val error: Error? = null) :
        Resource<Nothing>
}