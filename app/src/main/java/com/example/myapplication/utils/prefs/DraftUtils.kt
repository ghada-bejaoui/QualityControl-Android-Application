package com.example.myapplication.utils.prefs

import android.content.Context
import org.json.JSONException
import java.lang.Exception



class DraftUtils {
    companion object {


        val PRINTER = "printer"
        val ARTICLE = "article"

        //Gson().toJson()
        fun setDraft(context: Context, shared_name: String, key: String, value: String) {
            val sharedPref = context.getSharedPreferences(shared_name, 0)
            try {
                with(sharedPref.edit()) {
                    putString(key, value)
                    apply()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        /*fun convertStringToArticle(str: String): Article {
            return try{
                if (str.isNullOrEmpty() || str == "null"){
                    Article()
                }else{
                    val collectionType = object : TypeToken<Article>() {}.type
                    Gson().fromJson(str, collectionType)
                }

            }catch (e: JSONException){
                e.printStackTrace()
                Article ()
                throw e
            }catch (e: NullPointerException){
                e.printStackTrace()
                Article ()
                throw e
            }
        }*/

        fun getDraft(context: Context, shared_name: String, key: String): String {
            return try {
                val sharedPref = context.getSharedPreferences(shared_name, 0)
                sharedPref.getString(key, "").toString()
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
        fun removeDraft(context: Context,shared_name: String, key: String){
            val sharedPref = context.getSharedPreferences(shared_name, 0)
            try{
                with(sharedPref.edit()) {
                    remove(key)
                    apply()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        fun removeAllDraft(context: Context,shared_name: String){
            val sharedPref = context.getSharedPreferences(shared_name, 0)
            try{
                with(sharedPref.edit()) {
                    clear()
                    apply()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}