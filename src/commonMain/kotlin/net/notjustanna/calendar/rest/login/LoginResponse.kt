package net.notjustanna.calendar.rest.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val status: LoginResponseStatus,
    val token: String?
)

