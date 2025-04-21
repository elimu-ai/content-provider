package ai.elimu.content_provider.rest;

import java.util.List;

import ai.elimu.model.v2.gson.content.WordGson;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WordsService {

    @GET("content/words")
    Call<List<WordGson>> listWords();
}
