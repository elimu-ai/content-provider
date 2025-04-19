package ai.elimu.content_provider.ui.video

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.databinding.FragmentVideosBinding
import ai.elimu.content_provider.rest.VideosService
import ai.elimu.content_provider.room.GsonToRoomConverter.getVideo
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.content_provider.util.FileHelper.getVideoFile
import ai.elimu.content_provider.util.MultimediaDownloader
import ai.elimu.model.v2.gson.content.VideoGson
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
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors

class VideosFragment : Fragment() {
    private var videosViewModel: VideosViewModel? = null
    private lateinit var binding: FragmentVideosBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(javaClass.name, "onCreateView")

        videosViewModel = ViewModelProvider(this)[VideosViewModel::class.java]
        binding = FragmentVideosBinding.inflate(layoutInflater)
        videosViewModel!!.text.observe(viewLifecycleOwner, object : Observer<String?> {
            override fun onChanged(s: String?) {
                Log.i(javaClass.name, "onChanged")
                binding.textVideos.text = s
            }
        })
        return binding.root
    }

    override fun onStart() {
        Log.i(javaClass.name, "onStart")
        super.onStart()

        // Download Videos from REST API, and store them in the database
        val baseApplication = activity!!.application as BaseApplication
        val retrofit = baseApplication.retrofit
        val videosService = retrofit.create(VideosService::class.java)
        val videoGsonsCall = videosService.listVideos()
        Log.i(javaClass.name, "videoGsonsCall.request(): " + videoGsonsCall.request())
        videoGsonsCall.enqueue(object : Callback<List<VideoGson>> {
            override fun onResponse(
                call: Call<List<VideoGson>>,
                response: Response<List<VideoGson>>
            ) {
                Log.i(javaClass.name, "onResponse")

                Log.i(javaClass.name, "response: $response")
                if (response.isSuccessful) {
                    val videoGsons = response.body()!!
                    Log.i(javaClass.name, "videoGsons.size(): " + videoGsons.size)

                    if (videoGsons.isNotEmpty()) {
                        processResponseBody(videoGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(binding.textVideos, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    binding.progressBarVideos.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<VideoGson>>, t: Throwable) {
                Log.e(javaClass.name, "onFailure", t)

                Log.e(javaClass.name, "t.getCause():", t.cause)

                // Handle error
                Snackbar.make(binding.textVideos, t.cause.toString(), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                    .show()
                binding.progressBarVideos.visibility = View.GONE
            }
        })
    }

    private fun processResponseBody(videoGsons: List<VideoGson>) {
        Log.i(javaClass.name, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(object : Runnable {
            override fun run() {
                Log.i(javaClass.name, "run")

                val roomDb = RoomDb.getDatabase(context)
                val videoDao = roomDb.videoDao()

                //                Video_WordDao video_WordDao = roomDb.video_WordDao();

                // Empty the database table before downloading up-to-date content
//                video_WordDao.deleteAll();
                videoDao.deleteAll()

                // TODO: also delete corresponding video files (only those that are no longer used)
                for (videoGson in videoGsons) {
                    Log.i(javaClass.name, "videoGson.getId(): " + videoGson.id)

                    val video = getVideo(videoGson)

                    // Check if the corresponding video file has already been downloaded
                    val videoFile = getVideoFile(videoGson, context)
                    Log.i(javaClass.name, "videoFile: $videoFile")
                    Log.i(javaClass.name, "videoFile.exists(): " + videoFile!!.exists())
                    if (!videoFile.exists()) {
                        // Download file
                        val fileUrl = videoGson.fileUrl
                        Log.i(javaClass.name, "fileUrl: $fileUrl")
                        val bytes = MultimediaDownloader.downloadFileBytes(fileUrl)
                        Log.i(javaClass.name, "bytes.length: " + bytes.size)

                        // Store the downloaded file in the external storage directory
                        try {
                            val fileOutputStream = FileOutputStream(videoFile)
                            fileOutputStream.write(bytes)
                        } catch (e: FileNotFoundException) {
                            Log.e(javaClass.name, null, e)
                        } catch (e: IOException) {
                            Log.e(javaClass.name, null, e)
                        }
                        Log.i(javaClass.name, "videoFile.exists(): " + videoFile.exists())
                    }

                    // Store the Video in the database
                    videoDao.insert(video)
                    Log.i(javaClass.name, "Stored Video in database with ID " + video!!.id)

                    //                    // Store all the Video's Word labels in the database
//                    Set<WordGson> wordGsons = videoGson.getWords();
//                    Log.i(getClass().getName(), "wordGsons.size(): " + wordGsons.size());
//                    for (WordGson wordGson : wordGsons) {
//                        Log.i(getClass().getName(), "wordGson.getId(): " + wordGson.getId());
//                        Video_Word video_Word = new Video_Word();
//                        video_Word.setVideo_id(videoGson.getId());
//                        video_Word.setWords_id(wordGson.getId());
//                        video_WordDao.insert(video_Word);
//                        Log.i(getClass().getName(), "Stored Video_Word in database. Video_id: " + video_Word.getVideo_id() + ", words_id: " + video_Word.getWords_id());
//                    }
                }

                // Update the UI
                val videos = videoDao.loadAll()
                Log.i(javaClass.name, "videos.size(): " + videos.size)
                activity!!.runOnUiThread {
                    binding.textVideos.text = "videos.size(): " + videos.size
                    Snackbar.make(binding.textVideos, "videos.size(): " + videos.size, Snackbar.LENGTH_LONG)
                        .show()
                    binding.progressBarVideos.visibility = View.GONE
                }
            }
        })
    }
}