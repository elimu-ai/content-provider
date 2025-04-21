package ai.elimu.content_provider.rest

import ai.elimu.model.v2.gson.content.ImageGson
import retrofit2.Call
import retrofit2.http.GET

interface ImagesService {
    @GET("content/images")
    fun listImages(): Call<List<ImageGson>>
}
