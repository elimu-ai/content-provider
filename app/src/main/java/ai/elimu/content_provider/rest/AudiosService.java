package ai.elimu.content_provider.rest;

import java.util.List;

import ai.elimu.model.v2.gson.content.AudioGson;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AudiosService {

    @GET("content/audios")
    Call<List<AudioGson>> listAudios();
}
