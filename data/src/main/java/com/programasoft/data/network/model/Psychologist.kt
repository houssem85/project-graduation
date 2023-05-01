package com.programasoft.data.network.model

data class Psychologist(
    var id: Long,
    val account: Account,
    val city: String,
    val fullAddress: String,
    val image: String?,
    val phoneNumber: String,
    val presentation: String,
    val hourlyRate: Float,
    val messageRate: Float,
    var domains: Set<Domain> = emptySet(),
)