package com.programasoft.data.network.model

import kotlinx.serialization.Serializable


data class LoginResponse(
    val client: Client?,
    val psychologist: Psychologist?,
)