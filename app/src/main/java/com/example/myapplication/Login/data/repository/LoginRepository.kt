package com.example.myapplication.Login.data.repository

import com.example.myapplication.Login.data.api.LoginServices
import com.example.myapplication.Login.data.model.userData

class LoginRepository constructor(
    private val loginService : LoginServices
){

    suspend fun login(data: userData) =
        loginService.login(data)

}