package ai.elimu.content_provider.rest;

import java.util.List;

import ai.elimu.model.v2.gson.content.VideoGson;
import retrofit2.Call;
import retrofit2.http.GET;

public interface VideosService {

    @GET("content/videos")
    Call<List<VideoGson>> listVideos();
}
