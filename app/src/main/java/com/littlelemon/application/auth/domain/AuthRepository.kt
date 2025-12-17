package com.littlelemon.application.auth.domain

import com.littlelemon.application.core.domain.utils.Resource

interface AuthRepository {

    fun sendVerificationCode(email: String): Resource<Unit>

    fun validateVerificationCode(otp: String): Resource<Unit>

    fun saveUserInformation(firstName: String, lastName: String = ""): Resource<Unit>

    fun getLocation()
}