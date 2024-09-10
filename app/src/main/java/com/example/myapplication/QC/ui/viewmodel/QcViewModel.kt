package com.example.myapplication.QC.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.QC.data.repository.QcRepository
import com.example.myapplication.QC.data.model.HistoryItem
import com.example.myapplication.QC.data.model.Line
import com.example.myapplication.QC.data.model.Team
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class QcViewModel(private val repository: QcRepository) : ViewModel() {

    val loading = MutableLiveData(false)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handler, ${throwable.localizedMessage}")
    }


    private fun onError(message: String) {
        _errorMessage.postValue(message)
        loading.postValue(false)
    }

    private fun onWarning(message: String) {
        _warningMessage.postValue(message)
        loading.postValue(false)
    }

    // LiveData pour les sélections et les messages
    var selectedLine = MutableLiveData<String>()
    var selectedTeam = MutableLiveData<String>()

    // LiveData pour les lignes
    private val _lines = MutableLiveData<List<Line>>()
    val lines: LiveData<List<Line>> get() = _lines

    // LiveData pour les équipes
    private val _teams = MutableLiveData<List<Team>>()
    val teams: LiveData<List<Team>> get() = _teams

    // LiveData pour les données du formulaire
    val _saveFormDataResult = MutableLiveData<Any>()
    val saveFormDataResult: LiveData<Any> get() = _saveFormDataResult

    // LiveData pour l'historique
    private val _history = MutableLiveData<List<HistoryItem>>()
    val history: LiveData<List<HistoryItem>> get() = _history

    // LiveData pour les erreurs
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // LiveData pour les avertissements
    private val _warningMessage = MutableLiveData<String>()
    val warningMessage: LiveData<String> get() = _warningMessage

    // Chargement des lignes
    fun loadLines() {
        viewModelScope.launch(exceptionHandler) {
            loading.postValue(true)
            try {
                val linesData = repository.getLines()
                _lines.postValue(linesData)
                loading.postValue(false)
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to load lines: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Chargement des équipes
    fun loadTeams() {
        viewModelScope.launch(exceptionHandler) {
            loading.postValue(true)
            try {
                val teamsData = repository.getTeams()
                _teams.postValue(teamsData)
                loading.postValue(false)
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to load teams: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Enregistrement des données du formulaire
    fun saveFormData(data : HistoryItem) {
        viewModelScope.launch(exceptionHandler) {
            loading.postValue(true)
            try {
                val result = repository.saveFormData(data)
                _saveFormDataResult.postValue(result)
                _warningMessage.postValue("Form data saved successfully")
                loading.postValue(false)
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to save form data: ${e.message}")
                e.printStackTrace()
            }
        }
    }


}
