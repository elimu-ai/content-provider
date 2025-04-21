package ai.elimu.content_provider.rest

import ai.elimu.model.v2.gson.content.NumberGson
import retrofit2.Call
import retrofit2.http.GET

interface NumbersService {
    @GET("content/numbers")
    fun listNumbers(): Call<List<NumberGson>>
}
