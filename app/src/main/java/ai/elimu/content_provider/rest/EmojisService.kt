package ai.elimu.content_provider.rest

import ai.elimu.model.v2.gson.content.EmojiGson
import retrofit2.Call
import retrofit2.http.GET

interface EmojisService {
    @GET("content/emojis")
    fun listEmojis(): Call<List<EmojiGson>>
}
