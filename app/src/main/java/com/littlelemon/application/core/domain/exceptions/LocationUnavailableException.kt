package com.littlelemon.application.core.domain.exceptions

class LocationUnavailableException(message: String = "Unable to retrieve location. Ensure GPS is on and signal is available.") :
    Exception(message)