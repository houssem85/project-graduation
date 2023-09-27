package com.programasoft.data.network.di

import com.programasoft.data.network.NetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkApi(callFactory: Call.Factory): NetworkApi {
        return Retrofit.Builder()
            .baseUrl("http://192.168.8.103:8080/api/v1/")
            .callFactory(callFactory)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(NetworkApi::class.java)
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                },
        )
        .build()

}