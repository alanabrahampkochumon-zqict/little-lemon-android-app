package com.littlelemon.application.core.domain.exceptions

class RequestDeniedException(message: String? = null) : CoreException(message)
class InvalidRequestException(message: String? = null) : CoreException(message)
class UnknownException(message: String? = null) : CoreException(message)