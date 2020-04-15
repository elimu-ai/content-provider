package ai.elimu.content_provider.rest;

import java.util.List;

import ai.elimu.model.gson.v2.content.StoryBookGson;
import retrofit2.Call;
import retrofit2.http.GET;

public interface StoryBooksService {

    @GET("content/storybooks")
    Call<List<StoryBookGson>> listStoryBooks();
}
