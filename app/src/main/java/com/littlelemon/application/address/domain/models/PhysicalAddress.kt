package com.littlelemon.application.address.domain.models

data class PhysicalAddress(
    val address: String,
    val streetAddress: String,
    val city: String,
    val state: String,
    val pinCode: String,
)