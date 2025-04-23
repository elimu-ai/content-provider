package ai.elimu.content_provider.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import ai.elimu.model.v2.enums.Language;

public class SharedPreferencesHelper {

    public static final String SHARED_PREFS = "shared_prefs";

    public static final String PREF_APP_VERSION_CODE = "pref_app_version_code";
    public static final String PREF_LANGUAGE = "pref_language";

    public static void clearAllPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }


    public static void storeAppVersionCode(Context context, int appVersionCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(PREF_APP_VERSION_CODE, appVersionCode).apply();
    }

    public static int getAppVersionCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PREF_APP_VERSION_CODE, 0);
    }


    public static void storeLanguage(Context context, Language language) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_LANGUAGE, language.toString()).apply();
    }

    public static Language getLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String languageAsString = sharedPreferences.getString(PREF_LANGUAGE, null);
        if (TextUtils.isEmpty(languageAsString)) {
            return null;
        } else {
            try {
                return Language.valueOf(languageAsString);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}
