package ai.elimu.content_provider.rest;

import java.util.List;

import ai.elimu.model.v2.gson.content.NumberGson;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NumbersService {

    @GET("content/numbers")
    Call<List<NumberGson>> listNumbers();
}
