package com.example.myapplication.Login.data.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.ArrayList


class UserUtil {
  companion object {
    val PREFS = "UserPrefs"
    val CURRENT_USER = "currentuser"
    val KEY_IS_LOGGEDIN = "isLoggedIn"
    lateinit var editor: SharedPreferences.Editor


    fun saveCurrentUser(context: Context, response: User?, token: String) {
      val sharedPref = context.getSharedPreferences(PREFS, 0)
      try {
        val id: String = response?.id.toString()
        val idX3: String = response?.idX3.toString()
        val userName: String = response?.username.toString()
        val locations: String = response?.locations.toString()
        val permissions: ArrayList<String> = response?.permissions ?: ArrayList()
        val userEmail: String = response?.email.toString()
        val userRole: String = response?.permission.toString()
        val userEnabled: Boolean = response?.compteEnabled ?: true
        val isAdmin: Boolean = response?.isAdmin ?: false
        val role: ArrayList<Role> = response?.role ?: ArrayList()
        val sites: ArrayList<Site> = response?.sites ?: ArrayList()
        val cities: ArrayList<City> = response?.cities ?: ArrayList()
        val goverments: ArrayList<Site> = response?.goverments ?: ArrayList()

        val user = User(
          id,
          idX3,
          userEmail,
          userName,
          token,
          userRole,
          userEnabled,
          locations,
          permissions,
          role,
          sites,
          cities,
          goverments, "", isAdmin
        )
        with(sharedPref.edit()) {
          putString(CURRENT_USER, Gson().toJson(user))
          apply()
        }
      } catch (e: JSONException) {
        e.printStackTrace()
      }

    }
    fun getCurrentUser(context: Context): User{
      return try {
        val sharedPref = context.getSharedPreferences(PREFS, 0)
        val user = sharedPref.getString(CURRENT_USER, null)
        if (user != null){
          val gson = Gson()
          gson.fromJson(user, object : TypeToken<User>() {}.type)
        }else{
          User()
        }
      }catch (e: ExceptionInInitializerError){
        throw e
      }catch (e: Exception){
        throw e
      }
    }
    fun setLogin(context: Context, login:Boolean){
      val sharedPref = context.getSharedPreferences(PREFS, 0)
      try{
        with(sharedPref.edit()) {
          putBoolean(KEY_IS_LOGGEDIN, login)
          apply()
        }
      } catch (e: JSONException) {
        e.printStackTrace()
      }
    }
    fun getLogin(context: Context):Boolean{
      val sharedPref = context.getSharedPreferences(PREFS, 0)
      return sharedPref.getBoolean(KEY_IS_LOGGEDIN,false)
    }
    fun logoutUser(context: Context) { // Clearing all data from Shared Preferences
      val sharedPref = context.getSharedPreferences(PREFS, 0)
      editor = sharedPref.edit()
      editor.clear()
      editor.commit()
    }
  }
}
