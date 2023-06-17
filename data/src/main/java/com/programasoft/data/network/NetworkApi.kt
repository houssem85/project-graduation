package com.programasoft.data.network

import com.programasoft.data.network.model.AvailabilityGroup
import com.programasoft.data.network.model.AvailabilityRequest
import com.programasoft.data.network.model.AvailabilityUnit
import com.programasoft.data.network.model.Client
import com.programasoft.data.network.model.CreateReservationRequest
import com.programasoft.data.network.model.GetStatusResponse
import com.programasoft.data.network.model.LoginResponse
import com.programasoft.data.network.model.PaymeeResponce
import com.programasoft.data.network.model.Psychologist
import com.programasoft.data.network.model.ReservationReadyResponse
import com.programasoft.data.network.model.ReservationResponse
import com.programasoft.data.network.model.TransactionResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface NetworkApi {
    @POST(value = "accounts/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginResponse>

    @POST(value = "clients")
    suspend fun register(
        @Body client: Client,
    ): Response<Client>

    @GET("psychologists")
    suspend fun getPsychologists(): Response<List<Psychologist>>

    @GET("psychologists/{id}")
    suspend fun getPsychologist(@Path("id") id: Int): Response<Psychologist>


    @GET("availabilities/available-days")
    suspend fun getAvailableDays(
        @Query("psychologistId") psychologistId: Long,
    ): Response<List<String>>

    @GET("availabilities/available-units")
    suspend fun getAvailableUnits(
        @Query("psychologistId") psychologistId: Long,
        @Query("date") date: String,
    ): Response<List<AvailabilityUnit>>

    @POST("reservations")
    suspend fun createReservation(
        @Body reservationRequest: CreateReservationRequest,
    ): Response<ResponseBody>

    @GET("accounts/{id}/balance")
    suspend fun getBalance(
        @Path("id") accountId: Long,
    ): Response<Map<String, Double>>

    @POST
    suspend fun doPayment(
        @Url url: String,
        @Body json: com.google.gson.JsonObject,
        @Header("authorization") auth: String
    ): Response<PaymeeResponce>


    @GET
    suspend fun getPaymentStatus(
        @Url url: String,
        @Header("authorization") auth: String
    ): Response<GetStatusResponse>

    @POST("transactions")
    suspend fun paymentStore(
        @Body json: com.google.gson.JsonObject,
    ): Response<ResponseBody>

    @GET("transactions/recent/{accountId}")
    suspend fun getPaymentHistory(
        @Path("accountId") accountId: Long,
    ): Response<List<TransactionResponse>>

    @POST("availabilities")
    suspend fun createAvailabilities(
        @Body json: AvailabilityRequest,
    ): Response<ResponseBody>

    @GET("availabilities/by-psychologist/{psychologistId}")
    suspend fun getAvailabilityGroups(
        @Path("psychologistId") psychologistId: Long,
    ): Response<List<AvailabilityGroup>>

    @GET("reservations/client/{clientId}")
    suspend fun getReservations(
        @Path("clientId") clientId: Long,
        @Query("isPaid") isPaid: Boolean = true,
    ): Response<List<ReservationResponse>>

    @FormUrlEncoded
    @POST("transactions/payment-reservation")
    suspend fun paymentReservation(
        @Field("reservation_id") reservationId: Long
    ): Response<ResponseBody>

    @GET("reservations/ready/client/{clientId}")
    suspend fun getReadyReservations(
        @Path("clientId") clientId: Long
    ): Response<List<ReservationReadyResponse>>

    @GET("reservations/timeEnd/{id}")
    suspend fun getReservationEndTime(
        @Path("id") reservationId: Long
    ): Response<Long>

    @GET("reservations/ready/psychologist/{psychologistId}")
    suspend fun getReadyReservationsByPsychologist(
        @Path("psychologistId") psychologistId: Long
    ): Response<List<ReservationReadyResponse>>

    @DELETE("availabilities/{id}")
    suspend fun deleteAvailabilityGroup(
        @Query("id") id: Long,
    ): Response<ResponseBody>
}