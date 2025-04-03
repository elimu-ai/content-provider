package ai.elimu.content_provider.ui.letter

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.rest.LettersService
import ai.elimu.content_provider.room.GsonToRoomConverter
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.model.v2.gson.content.LetterGson
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

class LettersFragment : Fragment() {
    private var lettersViewModel: LettersViewModel? = null

    private var progressBar: ProgressBar? = null

    private var textView: TextView? = null

    private val TAG = javaClass.name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView")

        lettersViewModel = ViewModelProvider(this).get(
            LettersViewModel::class.java
        )
        val root = inflater.inflate(R.layout.fragment_letters, container, false)
        progressBar = root.findViewById(R.id.progress_bar_letters)
        textView = root.findViewById(R.id.text_letters) as? TextView
        lettersViewModel!!.text.observe(viewLifecycleOwner, object : Observer<String?> {
            override fun onChanged(s: String?) {
                Log.i(TAG, "onChanged")
                textView?.text = s
            }
        })
        return root
    }

    override fun onStart() {
        Log.i(TAG, "onStart")
        super.onStart()

        // Download Letters from REST API, and store them in the database
        val baseApplication = activity!!.application as BaseApplication
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
                    val letterGsons = response.body()!!
                    Log.i(TAG, "letterGsons.size(): " + letterGsons.size)

                    if (letterGsons.size > 0) {
                        processResponseBody(letterGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(textView!!, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<LetterGson>>, t: Throwable) {
                Log.e(TAG, "onFailure", t)

                Log.e(TAG, "t.getCause():", t.cause)

                // Handle error
                Snackbar.make(textView!!, t.cause.toString(), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                    .show()
                progressBar!!.visibility = View.GONE
            }
        })
    }

    private fun processResponseBody(letterGsons: List<LetterGson>) {
        Log.i(TAG, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(object : Runnable {
            override fun run() {
                Log.i(TAG, "run")

                val roomDb = RoomDb.getDatabase(context)
                val letterDao = roomDb.letterDao()

                // Empty the database table before downloading up-to-date content
                letterDao.deleteAll()

                for (letterGson in letterGsons) {
                    Log.i(TAG, "letterGson.getId(): " + letterGson.id)

                    // Store the Letter in the database
                    val letter = GsonToRoomConverter.getLetter(letterGson)
                    letter?.let {
                        letterDao.insert(letter)
                        Log.i(TAG, "Stored Letter in database with ID " + letter.id)
                    }
                }

                // Update the UI
                val letters = letterDao.loadAllOrderedByUsageCount()
                Log.i(TAG, "letters.size(): " + letters.size)
                activity!!.runOnUiThread {
                    textView!!.text = "letters.size(): " + letters.size
                    Snackbar.make(
                        textView!!,
                        "letters.size(): " + letters.size,
                        Snackbar.LENGTH_LONG
                    ).show()
                    progressBar!!.visibility = View.GONE
                }
            }
        })
    }
}
