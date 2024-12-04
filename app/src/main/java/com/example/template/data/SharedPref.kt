package com.example.template.data

import android.content.Context
import android.content.SharedPreferences

object SharedPrefs {
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        val sharedPreferences = context.getSharedPreferences(
            "shared_prefs_app",
            Context.MODE_PRIVATE
        )
        prefs = sharedPreferences
        editor = sharedPreferences.edit()
    }

    var isFirstInstall: Boolean
        get() = prefs.getBoolean("is_first_install", true)
        set(z) {
            editor.putBoolean("is_first_install", z).commit()
            editor.apply()
        }

    var languageCode: String
        get() = prefs.getString("language_code", "en") ?: "en"
        set(z) {
            editor.putString("language_code", z).commit()
            editor.apply()
        }
}

