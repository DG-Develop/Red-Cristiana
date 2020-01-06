package com.david.redcristianauno;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static final String STRING_PREFRENCES = "com.david.redcristianauno";
    public static final String PREFENCE_ESTADO_BUTTON_SESION = "estado.button.sesion";
    public static final String PREFERENCES_USUARIO_LOGIN = "usuarioprivado.login";
    public static final String PREFERENCES_ID_USUARIO = "usuario.id";


    public static void savePreferenceBoolean(Context c, boolean b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFRENCES,c.MODE_PRIVATE);
        preferences.edit().putBoolean(key, b).apply();
    }
    public static void savePreferenceString(Context c, String b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFRENCES,c.MODE_PRIVATE);
        preferences.edit().putString(key, b).apply();
    }

    public static boolean obtenerPreferencesBoolean(Context c, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFRENCES,c.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
    public static String obtenerPreferencesString(Context c, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFRENCES,c.MODE_PRIVATE);
        return preferences.getString(key, "");
    }
}
