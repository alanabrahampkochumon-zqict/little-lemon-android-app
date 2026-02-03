package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.core.domain.UseCase
import com.littlelemon.application.core.domain.utils.ValidationError
import com.littlelemon.application.core.domain.utils.ValidationPatterns
import com.littlelemon.application.core.domain.utils.ValidationResult

class ValidateEmailUseCase : UseCase<String, ValidationResult> {
    override suspend operator fun invoke(input: String): ValidationResult {

        val regex = ValidationPatterns.EMAIL_PATTERN

        return if (input.matches(regex)) {
            ValidationResult.Success
        } else if (input.isBlank() || input.isEmpty()) {
            ValidationResult.Failure(ValidationError.EmptyField, "Email cannot be empty")
        } else {
            ValidationResult.Failure(ValidationError.InvalidFormat, "Invalid email address")
        }
    }
}