package com.ankit.insiderapp.datarepo

import android.content.Context
import android.content.SharedPreferences
import com.ankit.insiderapp.R

object SharedPrefHelper {
    fun getPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.resources.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
    }

    fun writeToPref(context: Context, key: String, value: String) {
        val sharedPreferences = getPref(context)
        with(sharedPreferences.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun readFromPref(context: Context, key: String): String {
        val sharedPreferences = getPref(context)
        return sharedPreferences.getString(key,"")!!
    }
}