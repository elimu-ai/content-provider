package ai.elimu.content_provider.ui.sound

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.rest.SoundsService
import ai.elimu.content_provider.room.GsonToRoomConverter.getSound
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.model.v2.gson.content.SoundGson
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

class SoundsFragment : Fragment() {
    private var soundsViewModel: SoundsViewModel? = null

    private var progressBar: ProgressBar? = null

    private var textView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(javaClass.name, "onCreateView")

        soundsViewModel = ViewModelProvider(this).get(SoundsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sounds, container, false)
        progressBar = root.findViewById(R.id.progress_bar_sounds)
        textView = root.findViewById(R.id.text_sounds)
        soundsViewModel!!.getText().observe(viewLifecycleOwner, object : Observer<String?> {
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

        // Download Sounds from REST API, and store them in the database
        val baseApplication = activity!!.application as BaseApplication
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
                    val soundGsons = response.body()!!
                    Log.i(javaClass.name, "soundGsons.size(): " + soundGsons.size)

                    if (soundGsons.size > 0) {
                        processResponseBody(soundGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(textView!!, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<SoundGson>>, t: Throwable) {
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
                    val sound = getSound(soundGson)
                    soundDao.insert(sound)
                    Log.i(javaClass.name, "Stored Sound in database with ID " + sound!!.id)
                }

                // Update the UI
                val sounds = soundDao.loadAllOrderedByUsageCount()
                Log.i(javaClass.name, "sounds.size(): " + sounds.size)
                activity!!.runOnUiThread {
                    textView!!.text = "sounds.size(): " + sounds.size
                    Snackbar.make(textView!!, "sounds.size(): " + sounds.size, Snackbar.LENGTH_LONG)
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }
        })
    }
}
