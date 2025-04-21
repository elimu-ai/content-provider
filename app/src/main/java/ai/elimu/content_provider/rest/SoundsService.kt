package ai.elimu.content_provider.rest

import ai.elimu.model.v2.gson.content.SoundGson
import retrofit2.Call
import retrofit2.http.GET

interface SoundsService {
    @GET("content/sounds")
    fun listSounds(): Call<List<SoundGson>>
}
