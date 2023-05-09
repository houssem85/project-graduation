package com.programasoft.data.network

import com.programasoft.data.network.model.AvailabilityUnit
import com.programasoft.data.network.model.Client
import com.programasoft.data.network.model.CreateReservationRequest
import com.programasoft.data.network.model.LoginResponse
import com.programasoft.data.network.model.Psychologist
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
}