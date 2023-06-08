package com.programasoft.data.network.model

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    val id: Long,
    val date: String,
    val amount: Float,
    val type: String,
    @SerializedName("transactionId")
    val transactionId: String?
)