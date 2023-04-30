package com.programasoft.data.network.model

data class Account(
    var id: Long,
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val connectedDeviceId: String? = null,
)