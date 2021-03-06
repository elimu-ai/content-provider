package ai.elimu.content_provider.rest;

import java.util.List;

import ai.elimu.model.v2.gson.content.AllophoneGson;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AllophonesService {

    @GET("content/allophones")
    Call<List<AllophoneGson>> listAllophones();
}
