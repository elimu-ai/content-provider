package ai.elimu.content_provider.rest

import ai.elimu.model.v2.gson.content.VideoGson
import retrofit2.Call
import retrofit2.http.GET

interface VideosService {
    @GET("content/videos")
    fun listVideos(): Call<List<VideoGson>>
}
