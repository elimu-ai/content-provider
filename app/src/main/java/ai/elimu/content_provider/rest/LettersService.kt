package ai.elimu.content_provider.rest

import ai.elimu.model.v2.gson.content.LetterGson
import retrofit2.Call
import retrofit2.http.GET

interface LettersService {
    @GET("content/letters")
    fun listLetters(): Call<List<LetterGson>>
}
