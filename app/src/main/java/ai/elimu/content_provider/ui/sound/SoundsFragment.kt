package ai.elimu.content_provider.ui.sound

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.databinding.FragmentSoundsBinding
import ai.elimu.content_provider.rest.SoundsService
import ai.elimu.content_provider.room.GsonToRoomConverter.getSound
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.model.v2.gson.content.SoundGson
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class SoundsFragment : Fragment() {

    private lateinit var binding: FragmentSoundsBinding
    private lateinit var soundsViewModel: SoundsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(javaClass.name, "onCreateView")

        soundsViewModel = ViewModelProvider(this)[SoundsViewModel::class.java]
        binding = FragmentSoundsBinding.inflate(layoutInflater)
        soundsViewModel.getText().observe(viewLifecycleOwner, object : Observer<String?> {
            override fun onChanged(s: String?) {
                Log.i(javaClass.name, "onChanged")
                binding.textSounds.text = s
            }
        })
        return binding.root
    }

    override fun onStart() {
        Log.i(javaClass.name, "onStart")
        super.onStart()

        // Download Sounds from REST API, and store them in the database
        val baseApplication = activity?.application as? BaseApplication ?: return
        val retrofit = baseApplication.retrofit
        val soundsService = retrofit.create(SoundsService::class.java)
        val soundGsonsCall = soundsService.listSounds()
        Log.i(javaClass.name, "soundGsonsCall.request(): " + soundGsonsCall.request())
        soundGsonsCall.enqueue(object : Callback<List<SoundGson>> {
            override fun onResponse(
                call: Call<List<SoundGson>>,
                response: Response<List<SoundGson>>
            ) {
                Log.i(javaClass.name, "onResponse")

                Log.i(javaClass.name, "response: $response")
                if (response.isSuccessful) {
                    val soundGsons = response.body() ?: return
                    Log.i(javaClass.name, "soundGsons.size(): " + soundGsons.size)

                    if (soundGsons.isNotEmpty()) {
                        processResponseBody(soundGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(binding.textSounds, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    binding.progressBarSounds.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<SoundGson>>, t: Throwable) {
                Log.e(javaClass.name, "onFailure", t)

                Log.e(javaClass.name, "t.getCause():", t.cause)

                // Handle error
                Snackbar.make(binding.textSounds, t.cause.toString(), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                    .show()
                binding.progressBarSounds.visibility = View.GONE
            }
        })
    }

    private fun processResponseBody(soundGsons: List<SoundGson>) {
        Log.i(javaClass.name, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(object : Runnable {
            override fun run() {
                Log.i(javaClass.name, "run")

                val roomDb = RoomDb.getDatabase(context)
                val soundDao = roomDb.soundDao()

                // Empty the database table before downloading up-to-date content
                soundDao.deleteAll()

                for (soundGson in soundGsons) {
                    Log.i(javaClass.name, "soundGson.getId(): " + soundGson.id)

                    // Store the Sound in the database
                    getSound(soundGson)?.let { sound ->
                        soundDao.insert(sound)
                        Log.i(javaClass.name, "Stored Sound in! database with ID " + sound.id)
                    }
                }

                // Update the UI
                val sounds = soundDao.loadAllOrderedByUsageCount()
                Log.i(javaClass.name, "sounds.size(): " + sounds.size)
                activity?.runOnUiThread {
                    binding.textSounds.text = getString(R.string.sounds_size, sounds.size)
                    Snackbar.make(binding.textSounds, "sounds.size(): " + sounds.size, Snackbar.LENGTH_LONG)
                        .show()
                    binding.progressBarSounds.visibility = View.GONE
                }
            }
        })
    }
}
