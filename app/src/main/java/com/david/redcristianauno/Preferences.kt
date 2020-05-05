package com.david.redcristianauno

import android.content.Context

const val STRING_PREFRENCES = "com.david.redcristianauno"
const val PREFENCE_ESTADO_BUTTON_SESION = "estado.button.sesion"
const val PREFERENCES_USUARIO_NAME = "usuarioprivado.name"
const val PREFERENCES_ID_USUARIO = "usuario.id"

object Preferences {

    fun savePreferenceBoolean(c: Context, b: Boolean, key: String?) {
        val preferences = c.getSharedPreferences(STRING_PREFRENCES, Context.MODE_PRIVATE)
        preferences.edit().putBoolean(key, b).apply()
    }

   fun savePreferenceString(context: Context, message: String?, key: String?) {
        val preferences = context.getSharedPreferences(STRING_PREFRENCES, Context.MODE_PRIVATE)
        preferences.edit().putString(key, message).apply()
    }

    fun obtenerPreferencesBoolean(c: Context, key: String?): Boolean {
        val preferences = c.getSharedPreferences(STRING_PREFRENCES, Context.MODE_PRIVATE)
        return preferences.getBoolean(key, false)
    }

    fun obtenerPreferencesString(context: Context, key: String?): String? {
        val preferences = context.getSharedPreferences(STRING_PREFRENCES, Context.MODE_PRIVATE)
        return preferences.getString(key, "")
    }
}