package ai.elimu.content_provider.rest;

import java.util.List;

import ai.elimu.model.v2.gson.content.LetterGson;
import retrofit2.Call;
import retrofit2.http.GET;

public interface LettersService {

    @GET("content/letters")
    Call<List<LetterGson>> listLetters();
}
