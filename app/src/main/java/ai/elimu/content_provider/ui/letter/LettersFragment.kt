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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(javaClass.name, "onCreateView")

        lettersViewModel = ViewModelProvider(this).get(
            LettersViewModel::class.java
        )
        val root = inflater.inflate(R.layout.fragment_letters, container, false)
        progressBar = root.findViewById(R.id.progress_bar_letters)
        textView = root.findViewById(R.id.text_letters) as? TextView
        lettersViewModel!!.text.observe(viewLifecycleOwner, object : Observer<String?> {
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

        // Download Letters from REST API, and store them in the database
        val baseApplication = activity!!.application as BaseApplication
        val retrofit = baseApplication.retrofit
        val lettersService = retrofit.create(LettersService::class.java)
        val letterGsonsCall = lettersService.listLetters()
        Log.i(javaClass.name, "letterGsonsCall.request(): " + letterGsonsCall.request())
        letterGsonsCall.enqueue(object : Callback<List<LetterGson>> {
            override fun onResponse(
                call: Call<List<LetterGson>>,
                response: Response<List<LetterGson>>
            ) {
                Log.i(javaClass.name, "onResponse")

                Log.i(javaClass.name, "response: $response")
                if (response.isSuccessful) {
                    val letterGsons = response.body()!!
                    Log.i(javaClass.name, "letterGsons.size(): " + letterGsons.size)

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

    private fun processResponseBody(letterGsons: List<LetterGson>) {
        Log.i(javaClass.name, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(object : Runnable {
            override fun run() {
                Log.i(javaClass.name, "run")

                val roomDb = RoomDb.getDatabase(context)
                val letterDao = roomDb.letterDao()

                // Empty the database table before downloading up-to-date content
                letterDao.deleteAll()

                for (letterGson in letterGsons) {
                    Log.i(javaClass.name, "letterGson.getId(): " + letterGson.id)

                    // Store the Letter in the database
                    val letter = GsonToRoomConverter.getLetter(letterGson)
                    letterDao.insert(letter)
                    Log.i(javaClass.name, "Stored Letter in database with ID " + letter.id)
                }

                // Update the UI
                val letters = letterDao.loadAllOrderedByUsageCount()
                Log.i(javaClass.name, "letters.size(): " + letters.size)
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
