package com.example.myapplication.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkCheckUtils {
    companion object {
        @Suppress("DEPRECATION")
        fun isNetworkConnected(context: Context): Boolean {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm != null) {
                val activeNetwork = cm.activeNetworkInfo
                return activeNetwork != null && activeNetwork.isConnected
            }
            return false

        }
    }
}