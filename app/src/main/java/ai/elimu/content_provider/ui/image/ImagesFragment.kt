package ai.elimu.content_provider.ui.image

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.rest.ImagesService
import ai.elimu.content_provider.room.GsonToRoomConverter
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.content_provider.room.entity.Image_Word
import ai.elimu.content_provider.util.FileHelper
import ai.elimu.content_provider.util.MultimediaDownloader
import ai.elimu.model.v2.gson.content.ImageGson
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

class ImagesFragment : Fragment() {
    private var imagesViewModel: ImagesViewModel? = null

    private var progressBar: ProgressBar? = null

    private var textView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(javaClass.name, "onCreateView")

        imagesViewModel = ViewModelProvider(this).get(ImagesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_images, container, false)
        progressBar = root.findViewById(R.id.progress_bar_images)
        textView = root.findViewById(R.id.text_images) as? TextView
        imagesViewModel!!.text.observe(viewLifecycleOwner, object : Observer<String?> {
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

        // Download Images from REST API, and store them in the database
        val baseApplication = activity!!.application as BaseApplication
        val retrofit = baseApplication.retrofit
        val imagesService = retrofit.create(ImagesService::class.java)
        val imageGsonsCall = imagesService.listImages()
        Log.i(javaClass.name, "imageGsonsCall.request(): " + imageGsonsCall.request())
        imageGsonsCall.enqueue(object : Callback<List<ImageGson>> {
            override fun onResponse(
                call: Call<List<ImageGson>>,
                response: Response<List<ImageGson>>
            ) {
                Log.i(javaClass.name, "onResponse")

                Log.i(javaClass.name, "response: $response")
                if (response.isSuccessful) {
                    val imageGsons = response.body()!!
                    Log.i(javaClass.name, "imageGsons.size(): " + imageGsons.size)

                    if (imageGsons.size > 0) {
                        processResponseBody(imageGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(textView!!, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<ImageGson>>, t: Throwable) {
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

    private fun processResponseBody(imageGsons: List<ImageGson>) {
        Log.i(javaClass.name, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(object : Runnable {
            override fun run() {
                Log.i(javaClass.name, "run")

                val roomDb = RoomDb.getDatabase(context)
                val imageDao = roomDb.imageDao()
                val image_WordDao = roomDb.image_WordDao()

                // Empty the database table before downloading up-to-date content
                image_WordDao.deleteAll()
                imageDao.deleteAll()

                // TODO: also delete corresponding image files (only those that are no longer used)
                for (imageGson in imageGsons) {
                    Log.i(javaClass.name, "imageGson.getId(): " + imageGson.id)

                    val image = GsonToRoomConverter.getImage(imageGson)

                    // Check if the corresponding image file has already been downloaded
                    val imageFile = FileHelper.getImageFile(imageGson, context)
                    Log.i(javaClass.name, "imageFile: $imageFile")
                    Log.i(javaClass.name, "imageFile.exists(): " + imageFile.exists())
                    if (!imageFile.exists()) {
                        // Download file bytes
                        val baseApplication = activity!!.application as BaseApplication
                        val downloadUrl = if (imageGson.bytesUrl.startsWith("http"))
                            imageGson.bytesUrl
                        else
                            baseApplication.baseUrl + imageGson.bytesUrl
                        Log.i(javaClass.name, "downloadUrl: $downloadUrl")
                        val bytes = MultimediaDownloader.downloadFileBytes(downloadUrl)
                        Log.i(javaClass.name, "bytes.length: " + bytes.size)

                        // Store the downloaded file in the external storage directory
                        try {
                            val fileOutputStream = FileOutputStream(imageFile)
                            fileOutputStream.write(bytes)
                        } catch (e: FileNotFoundException) {
                            Log.e(javaClass.name, null, e)
                        } catch (e: IOException) {
                            Log.e(javaClass.name, null, e)
                        }
                        Log.i(javaClass.name, "imageFile.exists(): " + imageFile.exists())
                    }

                    // Store the Image in the database
                    imageDao.insert(image)
                    Log.i(javaClass.name, "Stored Image in database with ID " + image.id)

                    // Store all the Image's Word labels in the database
                    val wordGsons = imageGson.words
                    Log.i(javaClass.name, "wordGsons.size(): " + wordGsons.size)
                    for (wordGson in wordGsons) {
                        Log.i(javaClass.name, "wordGson.getId(): " + wordGson.id)
                        val image_Word = Image_Word()
                        image_Word.image_id = imageGson.id
                        image_Word.words_id = wordGson.id
                        image_WordDao.insert(image_Word)
                        Log.i(
                            javaClass.name,
                            "Stored Image_Word in database. Image_id: " + image_Word.image_id + ", words_id: " + image_Word.words_id
                        )
                    }
                }

                // Update the UI
                val images = imageDao.loadAll()
                Log.i(javaClass.name, "images.size(): " + images.size)
                activity!!.runOnUiThread {
                    textView!!.text = "images.size(): " + images.size
                    Snackbar.make(textView!!, "images.size(): " + images.size, Snackbar.LENGTH_LONG)
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }
        })
    }
}
