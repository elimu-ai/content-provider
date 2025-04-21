package ai.elimu.content_provider.rest;

import java.util.List;

import ai.elimu.model.v2.gson.content.ImageGson;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ImagesService {

    @GET("content/images")
    Call<List<ImageGson>> listImages();
}
