package com.example.myapplication.utils

import com.example.myapplication.BuildConfig
import com.example.myapplication.Login.data.api.LoginServices
import com.example.myapplication.QC.data.api.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
    private val BASE_URL: String = BuildConfig.BASE_URL

    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient : OkHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(60000, TimeUnit.SECONDS) // Augmentez le délai de lecture
            .connectTimeout(60000, TimeUnit.SECONDS) // Augmentez le délai de connexion
            .build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val loginService : LoginServices by lazy {
        retrofit.create(LoginServices::class.java)
    }

    val qcService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }


}