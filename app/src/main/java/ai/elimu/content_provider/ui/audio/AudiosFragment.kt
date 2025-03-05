package ai.elimu.content_provider.ui.audio

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.rest.AudiosService
import ai.elimu.content_provider.room.GsonToRoomConverter
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.content_provider.util.FileHelper
import ai.elimu.content_provider.util.MultimediaDownloader
import ai.elimu.model.v2.gson.content.AudioGson
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
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors

class AudiosFragment : Fragment() {
    private var audiosViewModel: AudiosViewModel? = null

    private var progressBar: ProgressBar? = null

    private var textView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(javaClass.name, "onCreateView")

        audiosViewModel = ViewModelProvider(this).get(AudiosViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_audios, container, false)
        progressBar = root.findViewById(R.id.progress_bar_audios)
        textView = root.findViewById(R.id.text_audios) as? TextView
        audiosViewModel!!.text.observe(viewLifecycleOwner, object : Observer<String?> {
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

        // Download Audios from REST API, and store them in the database
        val baseApplication = activity!!.application as BaseApplication
        val retrofit = baseApplication.retrofit
        val audiosService = retrofit.create(AudiosService::class.java)
        val audioGsonsCall = audiosService.listAudios()
        Log.i(javaClass.name, "audioGsonsCall.request(): " + audioGsonsCall.request())
        audioGsonsCall.enqueue(object : Callback<List<AudioGson>> {
            override fun onResponse(
                call: Call<List<AudioGson>>,
                response: Response<List<AudioGson>>
            ) {
                Log.i(javaClass.name, "onResponse")

                Log.i(javaClass.name, "response: $response")
                if (response.isSuccessful) {
                    val audioGsons = response.body()!!
                    Log.i(javaClass.name, "audioGsons.size(): " + audioGsons.size)

                    if (audioGsons.size > 0) {
                        processResponseBody(audioGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(textView!!, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<AudioGson>>, t: Throwable) {
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

    private fun processResponseBody(audioGsons: List<AudioGson>) {
        Log.i(javaClass.name, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(object : Runnable {
            override fun run() {
                Log.i(javaClass.name, "run")

                val roomDb = RoomDb.getDatabase(context)
                val audioDao = roomDb.audioDao()

                // Empty the database table before downloading up-to-date content
                audioDao.deleteAll()

                // TODO: also delete corresponding audio files (only those that are no longer used)
                for (audioGson in audioGsons) {
                    Log.i(javaClass.name, "audioGson.getId(): " + audioGson.id)

                    val audio = GsonToRoomConverter.getAudio(audioGson)

                    // Check if the corresponding audio file has already been downloaded
                    val audioFile = FileHelper.getAudioFile(audioGson, context)
                    Log.i(javaClass.name, "audioFile: $audioFile")
                    Log.i(javaClass.name, "audioFile.exists(): " + audioFile.exists())
                    if (!audioFile.exists()) {
                        // Download file bytes
                        val baseApplication = activity!!.application as BaseApplication
                        val downloadUrl = baseApplication.baseUrl + audioGson.bytesUrl
                        Log.i(javaClass.name, "downloadUrl: $downloadUrl")
                        val bytes = MultimediaDownloader.downloadFileBytes(downloadUrl)
                        Log.i(javaClass.name, "bytes.length: " + bytes.size)

                        // Store the downloaded file in the external storage directory
                        try {
                            val fileOutputStream = FileOutputStream(audioFile)
                            fileOutputStream.write(bytes)
                        } catch (e: FileNotFoundException) {
                            Log.e(javaClass.name, null, e)
                        } catch (e: IOException) {
                            Log.e(javaClass.name, null, e)
                        }
                        Log.i(javaClass.name, "audioFile.exists(): " + audioFile.exists())
                    }

                    // Store the Audio in the database
                    audioDao.insert(audio)
                    Log.i(javaClass.name, "Stored Audio in database with ID " + audio.id)
                }

                // Update the UI
                val audios = audioDao.loadAll()
                Log.i(javaClass.name, "audios.size(): " + audios.size)
                activity!!.runOnUiThread {
                    textView!!.text = "audios.size(): " + audios.size
                    Snackbar.make(textView!!, "audios.size(): " + audios.size, Snackbar.LENGTH_LONG)
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }
        })
    }
}
