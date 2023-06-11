package com.programasoft.data.network.model

import com.google.gson.annotations.SerializedName

data class AvailabilityRequest(
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    val days: Map<String, List<TimeInterval>>,
    @SerializedName("psychologist_id")
    val psychologistId: Long,
)

data class TimeInterval(
    val start: String,
    val end: String
)