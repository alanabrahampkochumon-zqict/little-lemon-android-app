package com.littlelemon.application.core.domain.validators

class MinLengthValidator(val minLength: Int) {
    operator fun invoke(value: String): Boolean = value.length >= minLength
}