package ai.elimu.content_provider;

import android.app.Application;
import android.util.Log;

import ai.elimu.content_provider.language.SharedPreferencesHelper;
import ai.elimu.model.v2.enums.Language;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate();
    }

    public Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getRestUrl() + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * E.g. "http://hin.test.elimu.ai"
     */
    public String getBaseUrl() {
        Language language = SharedPreferencesHelper.getLanguage(getApplicationContext());
        String url = "http://" + language.getIsoCode();
        if (!"release".equals(BuildConfig.BUILD_TYPE)) {
            url += ".test";
        }
        url += ".elimu.ai";
        return url;
    }

    public String getRestUrl() {
        return getBaseUrl() + "/rest/v2";
    }
}
