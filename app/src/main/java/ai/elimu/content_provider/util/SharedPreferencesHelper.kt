package ai.elimu.content_provider.util

import ai.elimu.model.v2.enums.Language
import android.content.Context
import android.text.TextUtils

object SharedPreferencesHelper {
    private const val SHARED_PREFS: String = "shared_prefs"

    private const val PREF_APP_VERSION_CODE: String = "pref_app_version_code"
    private const val PREF_LANGUAGE: String = "pref_language"

    fun clearAllPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }


    fun storeAppVersionCode(context: Context, appVersionCode: Int) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(PREF_APP_VERSION_CODE, appVersionCode).apply()
    }

    fun getAppVersionCode(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(PREF_APP_VERSION_CODE, 0)
    }


    fun storeLanguage(context: Context?, language: Language) {
        context ?: return
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(PREF_LANGUAGE, language.toString()).apply()
    }

    fun getLanguage(context: Context?): Language? {
        context ?: return null
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val languageAsString = sharedPreferences.getString(PREF_LANGUAGE, null)
        return if (TextUtils.isEmpty(languageAsString)) {
            null
        } else {
            try {
                Language.valueOf(languageAsString!!)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}
