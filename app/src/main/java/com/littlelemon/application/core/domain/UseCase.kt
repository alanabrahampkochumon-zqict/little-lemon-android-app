package com.littlelemon.application.core.domain

interface UseCase<INPUT, OUTPUT> {
    operator fun invoke(input: INPUT): OUTPUT
}