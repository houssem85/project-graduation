package com.programasoft.data.network.model

data class ReservationResponse(
    val id: Long,
    val psychologist: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val isPaid: Boolean = false,
    val amount: Float
)