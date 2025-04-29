package ai.elimu.content_provider.utils

import ai.elimu.model.v2.enums.Language
import android.content.Context
import android.util.Log
import androidx.core.net.toUri

object LanguageUtils {

    fun getLanguage(context: Context, providerId: String): Language? {
        val uri = ("content://" + providerId
                + ".provider.shared_data/shared_data").toUri()
        val cursor = context.contentResolver.query(uri,
            null, null, null, null
        )
        Log.v("getLanguage", "cursor: $cursor")
        cursor?.use {
            if (it.moveToNext()) {
                val contentProviderLanguage = it.getString(
                    it.getColumnIndexOrThrow(SharedDataKeys.KEY_LANGUAGE)
                )
                Log.d("getLanguage", "ContentProvider language: $contentProviderLanguage")
                return contentProviderLanguage.toLanguage()
            }
        }
        return null
    }
}

fun String?.toLanguage(): Language? {
    return when (this?.lowercase()) {
        Language.ENG.isoCode -> Language.ENG
        Language.HIN.isoCode -> Language.HIN
        Language.TGL.isoCode -> Language.TGL
        Language.THA.isoCode -> Language.THA
        else -> null
    }
}