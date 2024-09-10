package com.example.myapplication.utils.serviceState

import kotlinx.coroutines.flow.MutableStateFlow



data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        ANY
    }
    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }
        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }
        fun <T> error(message: String): Resource<T> {
            return Resource(Status.ERROR, null, message)
        }
        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
        fun <T> any(data: T? = null): Resource<T> {
            return Resource(Status.ANY, null, null)
        }
    }
}