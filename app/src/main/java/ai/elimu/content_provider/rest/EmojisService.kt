package ai.elimu.content_provider.rest;

import java.util.List;

import ai.elimu.model.v2.gson.content.EmojiGson;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EmojisService {

    @GET("content/emojis")
    Call<List<EmojiGson>> listEmojis();
}
