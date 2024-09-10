package com.example.myapplication.QC.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.QC.data.api.ApiService
import com.example.myapplication.QC.data.model.HistoryItem
import com.example.myapplication.QC.data.repository.QcRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class HistoriqueViewModel(private val repository: QcRepository) : ViewModel() {

    private val _history = MutableLiveData<List<HistoryItem>>()
    val history: LiveData<List<HistoryItem>> get() = _history

    // LiveData pour les erreurs
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // LiveData pour les avertissements
    private val _warningMessage = MutableLiveData<String>()
    val warningMessage: LiveData<String> get() = _warningMessage

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

    fun loadHistory(dateDebut: String?, dateFin: String?) {
        viewModelScope.launch(exceptionHandler) {
            loading.postValue(true)
            try {
                if (dateDebut.isNullOrEmpty() || dateFin.isNullOrEmpty()) {
                    _errorMessage.value = "Les dates de début et de fin sont obligatoires."
                    return@launch
                }

                val response = repository.getHistory(dateDebut, dateFin) // Adjust the repository call as needed
                if (response.isNotEmpty()) {
                    _history.value = response
                    _warningMessage.value = "" // Clear any warnings
                } else {
                    _history.value = emptyList()
                    _warningMessage.value = "Aucune donnée disponible pour les dates sélectionnées."
                }
                loading.postValue(false)
            } catch (e: Exception) {
                _errorMessage.value = "Échec du chargement des données."
            }
        }
    }
}