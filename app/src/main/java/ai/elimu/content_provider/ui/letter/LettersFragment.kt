package ai.elimu.content_provider.ui.letter

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.databinding.FragmentLettersBinding
import ai.elimu.content_provider.rest.LettersService
import ai.elimu.content_provider.room.GsonToRoomConverter
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.model.v2.gson.content.LetterGson
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

class LettersFragment : Fragment() {

    private lateinit var lettersViewModel: LettersViewModel
    private lateinit var binding: FragmentLettersBinding

    private val TAG = javaClass.name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")

        lettersViewModel = ViewModelProvider(this)[LettersViewModel::class.java]
        binding = FragmentLettersBinding.inflate(layoutInflater)
        lettersViewModel.getText().observe(viewLifecycleOwner) { s ->
            Log.i(TAG, "onChanged")
            binding.textLetters.text = s
        }
        return binding.root
    }

    override fun onStart() {
        Log.i(TAG, "onStart")
        super.onStart()

        // Download Letters from REST API, and store them in the database
        val baseApplication = activity?.application as? BaseApplication ?: return
        val retrofit = baseApplication.retrofit
        val lettersService = retrofit.create(LettersService::class.java)
        val letterGsonsCall = lettersService.listLetters()
        Log.i(TAG, "letterGsonsCall.request(): " + letterGsonsCall.request())
        letterGsonsCall.enqueue(object : Callback<List<LetterGson>> {
            override fun onResponse(
                call: Call<List<LetterGson>>,
                response: Response<List<LetterGson>>
            ) {
                Log.i(TAG, "onResponse")

                Log.i(TAG, "response: $response")
                if (response.isSuccessful) {
                    val letterGsons = response.body() ?: return
                    Log.i(TAG, "letterGsons.size(): " + letterGsons.size)

                    if (letterGsons.size > 0) {
                        processResponseBody(letterGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(binding.textLetters, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    binding.progressBarLetters.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<LetterGson>>, t: Throwable) {
                Log.e(TAG, "onFailure", t)

                Log.e(TAG, "t.getCause():", t.cause)

                // Handle error
                Snackbar.make(binding.textLetters, t.cause.toString(), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                    .show()
                binding.progressBarLetters.visibility = View.GONE
            }
        })
    }

    private fun processResponseBody(letterGsons: List<LetterGson>) {
        Log.i(TAG, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute {
            Log.i(TAG, "run")

            val roomDb = RoomDb.getDatabase(context)
            val letterDao = roomDb.letterDao()

            // Empty the database table before downloading up-to-date content
            letterDao.deleteAll()

            for (letterGson in letterGsons) {
                Log.i(TAG, "letterGson.getId(): " + letterGson.id)

                // Store the Letter in the database
                val letter = GsonToRoomConverter.getLetter(letterGson)
                letter.let {
                    letterDao.insert(letter)
                    Log.i(TAG, "Stored Letter in database with ID " + letter.id)
                }
            }

            // Update the UI
            val letters = letterDao.loadAllOrderedByUsageCount()
            Log.i(TAG, "letters.size(): " + letters.size)
            activity?.runOnUiThread {
                binding.textLetters.text = "letters.size(): " + letters.size
                Snackbar.make(
                    binding.textLetters,
                    "letters.size(): " + letters.size,
                    Snackbar.LENGTH_LONG
                ).show()
                binding.progressBarLetters.visibility = View.GONE
            }
        }
    }
}
