package net.notjustanna.calendar.rest.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val status: RegisterResponseStatus,
    val token: String?
)

