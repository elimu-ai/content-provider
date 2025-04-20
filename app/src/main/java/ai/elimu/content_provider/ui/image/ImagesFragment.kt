package ai.elimu.content_provider.ui.image

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.databinding.FragmentImagesBinding
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ImagesFragment : Fragment() {
    private lateinit var imagesViewModel: ImagesViewModel

    private lateinit var binding: FragmentImagesBinding

    private val TAG = ImagesFragment::class.java.name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")

        imagesViewModel = ViewModelProvider(this)[ImagesViewModel::class.java]
        binding = FragmentImagesBinding.inflate(layoutInflater)
        imagesViewModel.getText().observe(viewLifecycleOwner) { s ->
            Log.i(TAG, "onChanged")
            binding.textImages.text = s
        }
        return binding.root
    }

    override fun onStart() {
        Log.i(TAG, "onStart")
        super.onStart()

        // Download Images from REST API, and store them in the database
        val baseApplication = activity?.application as? BaseApplication ?: return
        val retrofit = baseApplication.retrofit
        val imagesService = retrofit.create(ImagesService::class.java)
        val imageGsonsCall = imagesService.listImages()
        Log.i(TAG, "imageGsonsCall.request(): " + imageGsonsCall.request())
        imageGsonsCall.enqueue(object : Callback<List<ImageGson>> {
            override fun onResponse(
                call: Call<List<ImageGson>>,
                response: Response<List<ImageGson>>
            ) {
                Log.i(TAG, "onResponse")

                Log.i(TAG, "response: $response")
                if (response.isSuccessful) {
                    val imageGsons = response.body() ?: return
                    Log.i(TAG, "imageGsons.size(): " + imageGsons.size)

                    if (imageGsons.isNotEmpty()) {
                        processResponseBody(imageGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(binding.textImages, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    binding.progressBarImages.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<ImageGson>>, t: Throwable) {
                Log.e(TAG, "onFailure", t)

                Log.e(TAG, "t.getCause():", t.cause)

                // Handle error
                Snackbar.make(binding.textImages, t.cause.toString(), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                    .show()
                binding.progressBarImages.visibility = View.GONE
            }
        })
    }

    private fun processResponseBody(imageGsons: List<ImageGson>) {
        Log.i(TAG, "processResponseBody")

        CoroutineScope(Dispatchers.IO).launch {
            Log.i(TAG, "run")

            val roomDb = RoomDb.getDatabase(context)
            val imageDao = roomDb.imageDao()
            val imageWordDao = roomDb.image_WordDao()

            // Empty the database table before downloading up-to-date content
            imageWordDao.deleteAll()
            imageDao.deleteAll()

            // TODO: also delete corresponding image files (only those that are no longer used)
            for (imageGson in imageGsons) {
                Log.i(TAG, "imageGson.getId(): " + imageGson.id)

                val image = GsonToRoomConverter.getImage(imageGson)

                // Check if the corresponding image file has already been downloaded
                val imageFile = FileHelper.getImageFile(imageGson, context)
                Log.i(TAG, "imageFile: $imageFile")
                Log.i(TAG, "imageFile.exists(): " + imageFile?.exists())
                if (imageFile?.exists() != true) {
                    // Download file bytes
                    val baseApplication = activity?.application as? BaseApplication ?: return@launch
                    val fileUrl = if (imageGson.fileUrl.startsWith("http"))
                        imageGson.fileUrl
                    else
                        baseApplication.baseUrl + imageGson.fileUrl
                    Log.i(TAG, "fileUrl: $fileUrl")
                    val bytes = MultimediaDownloader.downloadFileBytes(fileUrl)
                    Log.i(TAG, "bytes.length: " + bytes.size)

                    // Store the downloaded file in the external storage directory
                    try {
                        val fileOutputStream = FileOutputStream(imageFile)
                        fileOutputStream.write(bytes)
                    } catch (e: FileNotFoundException) {
                        Log.e(TAG, null, e)
                    } catch (e: IOException) {
                        Log.e(TAG, null, e)
                    }
                    Log.i(TAG, "imageFile.exists(): " + imageFile?.exists())
                }

                // Store the Image in the database
                image.let {
                    imageDao.insert(image)
                    Log.i(TAG, "Stored Image in database with ID " + image.id)
                }

                // Store all the Image's Word labels in the database
                val wordGsons = imageGson.words
                Log.i(TAG, "wordGsons.size(): " + wordGsons.size)
                for (wordGson in wordGsons) {
                    Log.i(TAG, "wordGson.getId(): " + wordGson.id)
                    val imageWord = Image_Word()
                    imageWord.image_id = imageGson.id
                    imageWord.words_id = wordGson.id
                    imageWordDao.insert(imageWord)
                    Log.i(
                        TAG,
                        "Stored Image_Word in database. Image_id: " + imageWord.image_id + ", words_id: " + imageWord.words_id
                    )
                }
            }

            // Update the UI
            val images = imageDao.loadAll()
            Log.i(TAG, "images.size(): " + images.size)
            withContext(Dispatchers.Main) {
                binding.textImages.text = getString(R.string.images_size, images.size)
                Snackbar.make(binding.textImages, "images.size(): " + images.size, Snackbar.LENGTH_LONG)
                    .show()
                binding.progressBarImages.visibility = View.GONE
            }
        }
    }
}
