package ai.elimu.content_provider.rest;

import java.util.List;

import ai.elimu.model.v2.gson.content.LetterSoundGson;
import retrofit2.Call;
import retrofit2.http.GET;

public interface LetterSoundsService {

    @GET("content/letter-sounds")
    Call<List<LetterSoundGson>> listLetterSounds();
}
