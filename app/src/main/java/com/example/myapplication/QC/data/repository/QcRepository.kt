package com.example.myapplication.QC.data.repository

import com.example.myapplication.QC.data.api.ApiService
import com.example.myapplication.QC.data.model.HistoryItem
import com.example.myapplication.QC.data.model.Line
import com.example.myapplication.QC.data.model.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QcRepository(private val apiService: ApiService) {

    // Retrieves the list of lines
    suspend fun getLines(): List<Line> = withContext(Dispatchers.IO) {
        apiService.getLines()
    }

    // Retrieves the list of teams
    suspend fun getTeams(): List<Team> = withContext(Dispatchers.IO) {
        apiService.getTeams()
    }

    // Saves form data
    suspend fun saveFormData(data : HistoryItem): Any = withContext(Dispatchers.IO) {
        apiService.saveFormData(data)
    }

    // Retrieves the history data based on a date range
    suspend fun getHistory(startDate: String, endDate: String): List<HistoryItem> = withContext(Dispatchers.IO) {
        apiService.getHistory(startDate, endDate)
    }
}
