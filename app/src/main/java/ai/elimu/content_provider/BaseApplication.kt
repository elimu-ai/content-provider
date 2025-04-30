package ai.elimu.content_provider

import ai.elimu.content_provider.util.SharedPreferencesHelper
import ai.elimu.content_provider.util.VersionHelper
import ai.elimu.model.v2.enums.Language
import android.app.Application
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BaseApplication : Application() {
    override fun onCreate() {
        Log.i(javaClass.name, "onCreate")
        super.onCreate()

        VersionHelper.updateAppVersion(applicationContext)
    }

    val retrofit: Retrofit
        get() {
            val retrofit = Retrofit.Builder()
                .baseUrl("$restUrl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }

    val baseUrl: String
        /**
         * E.g. "https://eng.elimu.ai" or "https://hin.elimu.ai"
         */
        get() {
            val language =
                SharedPreferencesHelper.getLanguage(applicationContext) ?: Language.ENG
            var url = "http://" + language.isoCode
            url += ".elimu.ai"
            return url
        }

    private val restUrl: String
        get() = "$baseUrl/rest/v2"
}
