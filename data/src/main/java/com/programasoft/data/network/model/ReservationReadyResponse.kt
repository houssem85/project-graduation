package com.programasoft.data.network.model

data class ReservationReadyResponse(
    val id: Long,
    val psychologist: String,
    val client: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val isReadyForJoin: Boolean = false
)