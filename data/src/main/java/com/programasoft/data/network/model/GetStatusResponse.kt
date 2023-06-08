package com.programasoft.data.network.model

import com.google.gson.annotations.SerializedName


data class GetStatusResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("code") val code: Int,
    @SerializedName("data") val data: GetStatusData
)

data class GetStatusData(
    @SerializedName("payment_status") val paymentStatus: Boolean,
    @SerializedName("token") val token: String,
    @SerializedName("amount") val amount: Int,
    @SerializedName("transaction_id") val transactionId: Int,
    @SerializedName("buyer_id") val buyerId: Int
)