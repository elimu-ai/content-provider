package ai.elimu.content_provider.rest

import ai.elimu.model.v2.gson.content.WordGson
import retrofit2.Call
import retrofit2.http.GET

interface WordsService {
    @GET("content/words")
    fun listWords(): Call<List<WordGson>>
}
