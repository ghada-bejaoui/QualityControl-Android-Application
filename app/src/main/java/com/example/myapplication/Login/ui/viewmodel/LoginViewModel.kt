package com.example.myapplication.Login.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.Login.data.model.UserResponse
import com.example.myapplication.Login.data.model.userData
import com.example.myapplication.Login.data.repository.LoginRepository
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class LoginViewModel  constructor(
    private val loginRepository: LoginRepository,
) : ViewModel() {

    var job: Job = Job()

    private val _loginResult = MutableLiveData<UserResponse>()
    val loginResult: LiveData<UserResponse> get() = _loginResult

    private var _errorMessage : MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> get() = _errorMessage

    val loading = MutableLiveData(false)

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private fun onError(message: String) {
        _errorMessage .postValue(message)
        loading.postValue(false)
    }

    fun resetValue() {
        _errorMessage  = MutableLiveData()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private val exeptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handler, ${throwable.localizedMessage}")
    }

    fun login(userData: userData) {
        job = CoroutineScope(Dispatchers.IO + exeptionHandler).launch {
            try {
                loading.postValue(true)
                val response = loginRepository.login(userData)
                if(response != null){
                    _loginResult.postValue(response)
                    loading.postValue(false)
                } else {
                    onError("Réponse de connexion null")
                }
            } catch(e: SocketTimeoutException){
                onError("La connexion au serveur a expiré. Veuillez réessayer.")
            } catch(e: Exception){
                onError("Erreur lors de la connexion : ${e.message}")
            }
        }
    }


}