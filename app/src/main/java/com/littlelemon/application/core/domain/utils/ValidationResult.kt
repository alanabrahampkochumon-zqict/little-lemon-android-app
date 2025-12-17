package com.littlelemon.application.core.domain.utils

sealed interface ValidationResult {
    data object Success : ValidationResult
    data class Failure(val error: ValidationError, val message: String) : ValidationResult
}