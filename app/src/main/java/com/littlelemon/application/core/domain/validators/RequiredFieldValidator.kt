package com.littlelemon.application.core.domain.validators

class RequiredFieldValidator {

    operator fun invoke(value: String): Boolean {
        return value.isNotBlank() && value.isNotEmpty()
    }
}