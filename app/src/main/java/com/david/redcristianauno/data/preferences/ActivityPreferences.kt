package com.david.redcristianauno.data.preferences

import android.content.Context

object ActivityPreferences {

    fun savePreferenceBoolean(context: Context, boolean: Boolean, key:String?){
        val preferences = context.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit().putBoolean(key, boolean).apply()
    }

    fun getPreferencesBoolean(context: Context, key: String?): Boolean{
        val preferences = context.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getBoolean(key, true)
    }

    const val PREFERENCES_STATE_ACTIVITY = "estate.activity"
    private const val STRING_PREFERENCES = "com.david.redcristianauno"
}