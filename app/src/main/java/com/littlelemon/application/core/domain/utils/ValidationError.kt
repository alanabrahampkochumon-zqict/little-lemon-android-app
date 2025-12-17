package com.littlelemon.application.core.domain.utils

sealed interface ValidationError {
    data object EmptyField : ValidationError
    data object InvalidFormat : ValidationError
    data object Unknown : ValidationError
}