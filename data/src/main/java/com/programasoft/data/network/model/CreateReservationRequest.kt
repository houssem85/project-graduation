package com.programasoft.data.network.model

data class CreateReservationRequest(
    val clientId: Long,
    val availabilityUnitIds: Set<Long>,
)