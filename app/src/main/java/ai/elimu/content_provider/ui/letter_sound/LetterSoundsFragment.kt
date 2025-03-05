package ai.elimu.content_provider.ui.letter_sound

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class LetterSoundsFragment : Fragment() {
    private var letterSoundsViewModel: LetterSoundsViewModel? = null

    private var progressBar: ProgressBar? = null

    private var textView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(javaClass.name, "onCreateView")

        letterSoundsViewModel = ViewModelProvider(this).get(
            LetterSoundsViewModel::class.java
        )
        val root = inflater.inflate(R.layout.fragment_letter_sounds, container, false)
        progressBar = root.findViewById(R.id.progress_bar_letter_sounds)
        textView = root.findViewById(R.id.text_letter_sounds) as? TextView
        letterSoundsViewModel!!.text.observe(viewLifecycleOwner, object : Observer<String?> {
            override fun onChanged(s: String?) {
                Log.i(javaClass.name, "onChanged")
                textView?.text = s
            }
        })
        return root
    }

    override fun onStart() {
        Log.i(javaClass.name, "onStart")
        super.onStart()

        // Download LetterSounds from REST API, and store them in the database
        val baseApplication = activity!!.application as BaseApplication
        val retrofit = baseApplication.retrofit
        val letterSoundsService = retrofit.create(
            LetterSoundsService::class.java
        )
        val letterSoundGsonsCall = letterSoundsService.listLetterSounds()
        Log.i(javaClass.name, "letterSoundGsonsCall.request(): " + letterSoundGsonsCall.request())
        letterSoundGsonsCall.enqueue(object : Callback<List<LetterSoundGson>> {
            override fun onResponse(
                call: Call<List<LetterSoundGson>>,
                response: Response<List<LetterSoundGson>>
            ) {
                Log.i(javaClass.name, "onResponse")

                Log.i(javaClass.name, "response: $response")
                if (response.isSuccessful) {
                    val letterSoundGsons = response.body()!!
                    Log.i(javaClass.name, "letterSoundGsons.size(): " + letterSoundGsons.size)

                    if (letterSoundGsons.size > 0) {
                        processResponseBody(letterSoundGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(textView!!, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<LetterSoundGson>>, t: Throwable) {
                Log.e(javaClass.name, "onFailure", t)

                Log.e(javaClass.name, "t.getCause():", t.cause)

                // Handle error
                Snackbar.make(textView!!, t.cause.toString(), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                    .show()
                progressBar!!.visibility = View.GONE
            }
        })
    }

    private fun processResponseBody(letterSoundGsons: List<LetterSoundGson>) {
        Log.i(javaClass.name, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(object : Runnable {
            override fun run() {
                Log.i(javaClass.name, "run")

                val roomDb = RoomDb.getDatabase(context)
                val letterSoundDao = roomDb.letterSoundDao()
                val letterSound_LetterDao = roomDb.letterSound_LetterDao()
                val letterSound_SoundDao = roomDb.letterSound_SoundDao()

                // Empty the database table before downloading up-to-date content
                letterSound_LetterDao.deleteAll()
                letterSound_SoundDao.deleteAll()
                letterSoundDao.deleteAll()

                for (letterSoundGson in letterSoundGsons) {
                    Log.i(javaClass.name, "letterSoundGson.getId(): " + letterSoundGson.id)

                    // Store the LetterSound in the database
                    val letterSound = GsonToRoomConverter.getLetterSound(letterSoundGson)
                    letterSoundDao.insert(letterSound)
                    Log.i(
                        javaClass.name,
                        "Stored LetterSound in database with ID " + letterSound.id
                    )

                    // Store all the LetterSound's letters in the database
                    val letterGsons = letterSoundGson.letters
                    Log.i(javaClass.name, "letterGsons.size(): " + letterGsons.size)
                    for (letterGson in letterGsons) {
                        Log.i(javaClass.name, "letterGson.getId(): " + letterGson.id)
                        val letterSound_Letter = LetterSound_Letter()
                        letterSound_Letter.letterSound_id = letterSoundGson.id
                        letterSound_Letter.letters_id = letterGson.id
                        letterSound_LetterDao.insert(letterSound_Letter)
                        Log.i(
                            javaClass.name,
                            "Stored LetterSound_Letter in database. LetterSound_id: " + letterSound_Letter.letterSound_id + ", letters_id: " + letterSound_Letter.letters_id
                        )
                    }

                    // Store all the LetterSound's sounds in the database
                    val soundGsons = letterSoundGson.sounds
                    Log.i(javaClass.name, "soundGsons.size():" + soundGsons.size)
                    for (soundGson in soundGsons) {
                        Log.i(javaClass.name, "soundGson.getId(): " + soundGson.id)
                        val letterSound_Sound = LetterSound_Sound()
                        letterSound_Sound.letterSound_id = letterSoundGson.id
                        letterSound_Sound.sounds_id = soundGson.id
                        letterSound_SoundDao.insert(letterSound_Sound)
                        Log.i(
                            javaClass.name,
                            "Stored LetterSound_Sound in database. LetterSound_id: " + letterSound_Sound.letterSound_id + ", sounds_id: " + letterSound_Sound.sounds_id
                        )
                    }
                }

                // Update the UI
                val letterSounds = letterSoundDao.loadAll()
                Log.i(javaClass.name, "letterSounds.size(): " + letterSounds.size)
                activity!!.runOnUiThread {
                    textView!!.text = "letterSounds.size(): " + letterSounds.size
                    Snackbar.make(
                        textView!!,
                        "letterSounds.size(): " + letterSounds.size,
                        Snackbar.LENGTH_LONG
                    ).show()
                    progressBar!!.visibility = View.GONE
                }
            }
        })
    }
}
