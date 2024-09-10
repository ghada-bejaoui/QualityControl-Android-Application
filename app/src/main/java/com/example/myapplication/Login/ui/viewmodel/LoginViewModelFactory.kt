package com.example.myapplication.Login.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Login.data.repository.LoginRepository

class LoginViewModelFactory constructor(
    private val loginRepository: LoginRepository
) : ViewModelProvider.NewInstanceFactory()  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(this.loginRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}
