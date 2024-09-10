package com.example.myapplication.QC.data.api

import com.example.myapplication.QC.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("/lines")
    suspend fun getLines(): List<Line>

    @GET("/teams")
    suspend fun getTeams(): List<Team>

    @POST("/histories")
    suspend fun saveFormData(
        @Body data: HistoryItem
    )

    @GET("/histories")
    suspend fun getHistory(
        @Query("debut") debut: String,
        @Query("fin") fin: String,
    ): List<HistoryItem>
}
