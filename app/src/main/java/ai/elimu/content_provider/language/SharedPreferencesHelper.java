package ai.elimu.content_provider.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import ai.elimu.model.enums.Language;

public class SharedPreferencesHelper {

    public static Language getLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        String languageAsString = sharedPreferences.getString("language", null);
        if (TextUtils.isEmpty(languageAsString)) {
            return null;
        } else {
            return Language.valueOf(languageAsString);
        }
    }
}
