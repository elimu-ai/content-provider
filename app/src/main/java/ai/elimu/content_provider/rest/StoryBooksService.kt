package ai.elimu.content_provider.rest

import ai.elimu.model.v2.gson.content.StoryBookGson
import retrofit2.Call
import retrofit2.http.GET

interface StoryBooksService {
    @GET("content/storybooks")
    fun listStoryBooks(): Call<List<StoryBookGson>>
}
