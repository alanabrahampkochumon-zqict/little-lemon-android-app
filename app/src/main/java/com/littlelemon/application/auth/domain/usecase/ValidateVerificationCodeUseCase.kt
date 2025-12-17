package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.core.domain.UseCase
import com.littlelemon.application.core.domain.utils.ValidationError
import com.littlelemon.application.core.domain.utils.ValidationResult

class ValidateVerificationCodeUseCase : UseCase<String, ValidationResult> {

    override suspend fun invoke(input: String): ValidationResult {
        val regex = Regex("^[0-9]{4}$")
        return if (input.matches(regex)) {
            ValidationResult.Success
        } else {
            ValidationResult.Failure(
                ValidationError.InvalidFormat,
                "Invalid Verification Code"
            )
        }
    }


}