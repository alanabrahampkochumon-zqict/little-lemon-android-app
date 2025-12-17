package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.core.domain.UseCase
import com.littlelemon.application.core.domain.utils.ValidationError
import com.littlelemon.application.core.domain.utils.ValidationResult

class ValidateFirstNameUseCase : UseCase<String, ValidationResult> {

    override suspend fun invoke(input: String): ValidationResult {
        return if (input.isEmpty() || input.isBlank()) {
            ValidationResult.Failure(ValidationError.EmptyField, "First name cannot be empty")
        } else if (input.length < 2) {
            ValidationResult.Failure(
                ValidationError.EmptyField,
                "First name needs to be more than one letter"
            )
        } else {
            ValidationResult.Success
        }
    }
    
}