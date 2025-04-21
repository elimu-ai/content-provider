package ai.elimu.content_provider.rest

import ai.elimu.model.v2.gson.content.LetterSoundGson
import retrofit2.Call
import retrofit2.http.GET

interface LetterSoundsService {
    @GET("content/letter-sounds")
    fun listLetterSounds(): Call<List<LetterSoundGson>>
}
