package com.example.template.data.models

import com.example.template.R


class AppLanguage(
    var name: String,
    var code: String,
    var flag: Int
) {

    companion object {
        val appSupportedLanguages: ArrayList<AppLanguage>
            get() {
                return arrayListOf(
                    AppLanguage("Français", "fr", R.drawable.ic_language_fr),
                    AppLanguage("中国人", "zh", R.drawable.ic_language_zh),
                    AppLanguage("Indonesia", "in", R.drawable.ic_language_id),
                    AppLanguage("जर्मन", "hi", R.drawable.ic_language_hi),
                    AppLanguage("Deutsch", "de", R.drawable.ic_language_de),
                    AppLanguage("Español", "es", R.drawable.ic_language_spain),
                    AppLanguage("日本語", "ja", R.drawable.ic_language_japan),
                    AppLanguage("عربي", "ar", R.drawable.ic_language_arap),
                    AppLanguage("Português", "pt", R.drawable.ic_language_portugal),
                    AppLanguage("English", "en", R.drawable.ic_language_en)
                )
            }
    }
}