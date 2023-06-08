package com.programasoft.data.network.model

import com.google.gson.annotations.SerializedName

data class PaymeeResponce(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("code") val code: Int,
    @SerializedName("data") val data: Data
)

data class Data(
    @SerializedName("token") val token: String,
    @SerializedName("order_id") val orderId: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("note") val note: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("payment_url") val paymentUrl: String
)