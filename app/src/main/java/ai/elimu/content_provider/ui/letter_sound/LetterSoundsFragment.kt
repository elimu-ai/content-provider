package ai.elimu.content_provider.ui.letter_sound

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.databinding.FragmentLetterSoundsBinding
import ai.elimu.content_provider.rest.LetterSoundsService
import ai.elimu.content_provider.room.GsonToRoomConverter
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.content_provider.room.entity.LetterSound_Letter
import ai.elimu.content_provider.room.entity.LetterSound_Sound
import ai.elimu.model.v2.gson.content.LetterSoundGson
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class LetterSoundsFragment : Fragment() {

    private var letterSoundsViewModel: LetterSoundsViewModel? = null
    private lateinit var binding: FragmentLetterSoundsBinding

    private val TAG = javaClass.name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")

        letterSoundsViewModel = ViewModelProvider(this)[LetterSoundsViewModel::class.java]
        binding = FragmentLetterSoundsBinding.inflate(layoutInflater)
        letterSoundsViewModel?.text?.observe(viewLifecycleOwner) { s ->
            Log.i(TAG, "onChanged")
            binding.textLetterSounds.text = s
        }
        return binding.root
    }

    override fun onStart() {
        Log.i(TAG, "onStart")
        super.onStart()

        // Download LetterSounds from REST API, and store them in the database
        val baseApplication = activity?.application as? BaseApplication ?: return
        val retrofit = baseApplication.retrofit
        val letterSoundsService = retrofit.create(
            LetterSoundsService::class.java
        )
        val letterSoundGsonsCall = letterSoundsService.listLetterSounds()
        Log.i(TAG, "letterSoundGsonsCall.request(): " + letterSoundGsonsCall.request())
        letterSoundGsonsCall.enqueue(object : Callback<List<LetterSoundGson>> {
            override fun onResponse(
                call: Call<List<LetterSoundGson>>,
                response: Response<List<LetterSoundGson>>
            ) {
                Log.i(TAG, "onResponse")

                Log.i(TAG, "response: $response")
                if (response.isSuccessful) {
                    val letterSoundGsons = response.body() ?: return
                    Log.i(TAG, "letterSoundGsons.size(): " + letterSoundGsons.size)

                    if (letterSoundGsons.isNotEmpty()) {
                        processResponseBody(letterSoundGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(binding.textLetterSounds, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    binding.progressBarLetterSounds.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<LetterSoundGson>>, t: Throwable) {
                Log.e(TAG, "onFailure", t)

                Log.e(TAG, "t.getCause():", t.cause)

                // Handle error
                Snackbar.make(binding.textLetterSounds, t.cause.toString(), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                    .show()
                binding.progressBarLetterSounds.visibility = View.GONE
            }
        })
    }

    private fun processResponseBody(letterSoundGsons: List<LetterSoundGson>) {
        Log.i(TAG, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute {
            Log.i(TAG, "run")

            val roomDb = RoomDb.getDatabase(context)
            val letterSoundDao = roomDb.letterSoundDao()
            val letterSoundLetterDao = roomDb.letterSound_LetterDao()
            val letterSoundSoundDao = roomDb.letterSound_SoundDao()

            // Empty the database table before downloading up-to-date content
            letterSoundLetterDao.deleteAll()
            letterSoundSoundDao.deleteAll()
            letterSoundDao.deleteAll()

            for (letterSoundGson in letterSoundGsons) {
                Log.i(TAG, "letterSoundGson.getId(): " + letterSoundGson.id)

                // Store the LetterSound in the database
                val letterSound = GsonToRoomConverter.getLetterSound(letterSoundGson)
                letterSound.let {
                    letterSoundDao.insert(letterSound)
                    Log.i(
                        TAG,
                        "Stored LetterSound in database with ID " + letterSound.id
                    )
                }

                // Store all the LetterSound's letters in the database
                val letterGsons = letterSoundGson.letters
                Log.i(TAG, "letterGsons.size(): " + letterGsons.size)
                for ((index, letterGson) in letterGsons.withIndex()) {
                    Log.i(TAG, "letterGson.getId(): " + letterGson.id)
                    val letterSoundLetter = LetterSound_Letter()
                    letterSoundLetter.letterSound_id = letterSoundGson.id
                    letterSoundLetter.letters_id = letterGson.id
                    letterSoundLetter.letters_ORDER = index
                    letterSoundLetterDao.insert(letterSoundLetter)
                    Log.i(
                        TAG,
                        "Stored LetterSound_Letter in database. LetterSound_id: " + letterSoundLetter.letterSound_id + ", letters_id: " + letterSoundLetter.letters_id
                    )
                }

                // Store all the LetterSound's sounds in the database
                val soundGsons = letterSoundGson.sounds
                Log.i(TAG, "soundGsons.size():" + soundGsons.size)
                for ((index, soundGson) in soundGsons.withIndex()) {
                    Log.i(TAG, "soundGson.getId(): " + soundGson.id)
                    val letterSoundSound = LetterSound_Sound()
                    letterSoundSound.letterSound_id = letterSoundGson.id
                    letterSoundSound.sounds_id = soundGson.id
                    letterSoundSound.sounds_ORDER = index
                    letterSoundSoundDao.insert(letterSoundSound)
                    Log.i(
                        TAG,
                        "Stored LetterSound_Sound in database. LetterSound_id: " + letterSoundSound.letterSound_id + ", sounds_id: " + letterSoundSound.sounds_id
                    )
                }
            }

            // Update the UI
            val letterSounds = letterSoundDao.loadAll()
            Log.i(TAG, "letterSounds.size(): " + letterSounds.size)
            activity?.runOnUiThread {
                binding.textLetterSounds.text =
                    getString(R.string.lettersounds_size, letterSounds.size)
                Snackbar.make(
                    binding.textLetterSounds,
                    "letterSounds.size(): " + letterSounds.size,
                    Snackbar.LENGTH_LONG
                ).show()
                binding.progressBarLetterSounds.visibility = View.GONE
            }
        }
    }
}
