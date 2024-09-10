package com.example.myapplication.Login.data.api

import com.example.myapplication.Login.data.model.*
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginServices {

    @POST("/users/login")
    suspend fun login(@Body userData: userData): UserResponse

}