package ai.elimu.content_provider.ui.word

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.rest.WordsService
import ai.elimu.content_provider.room.GsonToRoomConverter.getWord
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.model.v2.gson.content.WordGson
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

class WordsFragment : Fragment() {
    private var wordsViewModel: WordsViewModel? = null

    private var progressBar: ProgressBar? = null

    private var textView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(javaClass.name, "onCreateView")

        wordsViewModel = ViewModelProvider(this)[WordsViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_words, container, false)
        progressBar = root.findViewById(R.id.progress_bar_words)
        textView = root.findViewById(R.id.text_words)
        wordsViewModel!!.text.observe(viewLifecycleOwner, object : Observer<String?> {
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

        // Download Words from REST API, and store them in the database
        val baseApplication = activity!!.application as BaseApplication
        val retrofit = baseApplication.retrofit
        val wordsService = retrofit.create(WordsService::class.java)
        val wordGsonsCall = wordsService.listWords()
        Log.i(javaClass.name, "wordGsonsCall.request(): " + wordGsonsCall.request())
        wordGsonsCall.enqueue(object : Callback<List<WordGson>> {
            override fun onResponse(
                call: Call<List<WordGson>>,
                response: Response<List<WordGson>>
            ) {
                Log.i(javaClass.name, "onResponse")

                Log.i(javaClass.name, "response: $response")
                if (response.isSuccessful) {
                    val wordGsons = response.body()!!
                    Log.i(javaClass.name, "wordGsons.size(): " + wordGsons.size)

                    if (wordGsons.isNotEmpty()) {
                        processResponseBody(wordGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(textView!!, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<WordGson>>, t: Throwable) {
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

    private fun processResponseBody(wordGsons: List<WordGson>) {
        Log.i(javaClass.name, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(object : Runnable {
            override fun run() {
                Log.i(javaClass.name, "run")

                val roomDb = RoomDb.getDatabase(context)
                val wordDao = roomDb.wordDao()

                // Empty the database table before downloading up-to-date content
                wordDao.deleteAll()

                for (wordGson in wordGsons) {
                    Log.i(javaClass.name, "wordGson.getId(): " + wordGson.id)

                    // Store the Word in the database
                    val word = getWord(wordGson)
                    wordDao.insert(word)
                    Log.i(javaClass.name, "Stored Word in database with ID " + word!!.id)
                }

                // Update the UI
                val words = wordDao.loadAllOrderedByUsageCount()
                Log.i(javaClass.name, "words.size(): " + words.size)
                activity!!.runOnUiThread {
                    textView!!.text = "words.size(): " + words.size
                    Snackbar.make(textView!!, "words.size(): " + words.size, Snackbar.LENGTH_LONG)
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }
        })
    }
}
