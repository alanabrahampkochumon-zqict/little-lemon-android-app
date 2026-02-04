package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.core.domain.UseCase
import com.littlelemon.application.core.domain.utils.ValidationError
import com.littlelemon.application.core.domain.utils.ValidationPatterns
import com.littlelemon.application.core.domain.utils.ValidationResult

class ValidateOTPUseCase : UseCase<String, ValidationResult> {

    override suspend fun invoke(input: String): ValidationResult {
        val regex = ValidationPatterns.OTP_PATTERN
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