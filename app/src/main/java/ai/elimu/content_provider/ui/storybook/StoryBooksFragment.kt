package ai.elimu.content_provider.ui.storybook

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.rest.StoryBooksService
import ai.elimu.content_provider.room.GsonToRoomConverter.getStoryBook
import ai.elimu.content_provider.room.GsonToRoomConverter.getStoryBookChapter
import ai.elimu.content_provider.room.GsonToRoomConverter.getStoryBookParagraph
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.content_provider.room.entity.StoryBookParagraph_Word
import ai.elimu.model.v2.gson.content.StoryBookGson
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

class StoryBooksFragment : Fragment() {
    private var storyBooksViewModel: StoryBooksViewModel? = null

    private var progressBar: ProgressBar? = null

    private var textView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(javaClass.name, "onCreateView")

        storyBooksViewModel = ViewModelProvider(this).get(
            StoryBooksViewModel::class.java
        )
        val root = inflater.inflate(R.layout.fragment_storybooks, container, false)
        progressBar = root.findViewById(R.id.progress_bar_storybooks)
        textView = root.findViewById(R.id.text_storybooks)
        storyBooksViewModel!!.text.observe(viewLifecycleOwner, object : Observer<String?> {
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

        // Download StoryBooks from REST API, and store them in the database
        val baseApplication = activity!!.application as BaseApplication
        val retrofit = baseApplication.retrofit
        val storyBooksService = retrofit.create(
            StoryBooksService::class.java
        )
        val call = storyBooksService.listStoryBooks()
        Log.i(javaClass.name, "call.request(): " + call.request())
        call.enqueue(object : Callback<List<StoryBookGson>> {
            override fun onResponse(
                call: Call<List<StoryBookGson>>,
                response: Response<List<StoryBookGson>>
            ) {
                Log.i(javaClass.name, "onResponse")

                Log.i(javaClass.name, "response: $response")
                if (response.isSuccessful) {
                    val storyBookGsons = response.body()!!
                    Log.i(javaClass.name, "storyBookGsons.size(): " + storyBookGsons.size)

                    if (storyBookGsons.size > 0) {
                        processResponseBody(storyBookGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(textView!!, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<StoryBookGson>>, t: Throwable) {
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

    private fun processResponseBody(storyBookGsons: List<StoryBookGson>) {
        Log.i(javaClass.name, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(object : Runnable {
            override fun run() {
                Log.i(javaClass.name, "run")

                val roomDb = RoomDb.getDatabase(context)
                val storyBookDao = roomDb.storyBookDao()
                val storyBookChapterDao = roomDb.storyBookChapterDao()
                val storyBookParagraphDao = roomDb.storyBookParagraphDao()
                val storyBookParagraph_WordDao = roomDb.storyBookParagraph_WordDao()

                // Empty the database table before downloading up-to-date content
                storyBookParagraph_WordDao.deleteAll()
                storyBookParagraphDao.deleteAll()
                storyBookChapterDao.deleteAll()
                storyBookDao.deleteAll()

                for (storyBookGson in storyBookGsons) {
                    Log.i(javaClass.name, "storyBookGson.getId(): " + storyBookGson.id)

                    // Store the StoryBook in the database
                    val storyBook = getStoryBook(storyBookGson)
                    storyBookDao.insert(storyBook)
                    Log.i(javaClass.name, "Stored StoryBook in database with ID " + storyBook!!.id)

                    val storyBookChapterGsons = storyBookGson.storyBookChapters
                    Log.i(
                        javaClass.name,
                        "storyBookChapterGsons.size(): " + storyBookChapterGsons.size
                    )
                    for (storyBookChapterGson in storyBookChapterGsons) {
                        val storyBookChapter = getStoryBookChapter(storyBookChapterGson)
                        storyBookChapter!!.storyBookId = storyBookGson.id
                        storyBookChapterDao.insert(storyBookChapter)
                        Log.i(
                            javaClass.name,
                            "Stored StoryBookChapter in database with ID " + storyBookChapter.id
                        )

                        val storyBookParagraphs = storyBookChapterGson.storyBookParagraphs
                        Log.i(
                            javaClass.name,
                            "storyBookParagraphs.size(): " + storyBookParagraphs.size
                        )
                        for (storyBookParagraphGson in storyBookParagraphs) {
                            val storyBookParagraph = getStoryBookParagraph(storyBookParagraphGson)
                            storyBookParagraph!!.storyBookChapterId = storyBookChapterGson.id
                            storyBookParagraphDao.insert(storyBookParagraph)
                            Log.i(
                                javaClass.name,
                                "Stored StoryBookParagraph in database with ID " + storyBookParagraph.id
                            )

                            // Store all the StoryBookParagraph's Words in the database
                            val wordGsons = storyBookParagraphGson.words
                            Log.i(javaClass.name, "wordGsons.size(): " + wordGsons.size)
                            for (i in wordGsons.indices) {
                                val wordGson = wordGsons[i]
                                Log.i(javaClass.name, "wordGson: $wordGson")
                                if (wordGson != null) {
                                    Log.i(javaClass.name, "wordGson.getId(): " + wordGson.id)
                                    val storyBookParagraph_Word = StoryBookParagraph_Word()
                                    storyBookParagraph_Word.storyBookParagraph_id =
                                        storyBookParagraphGson.id
                                    storyBookParagraph_Word.words_id = wordGson.id
                                    storyBookParagraph_Word.words_ORDER = i
                                    storyBookParagraph_WordDao.insert(storyBookParagraph_Word)
                                    Log.i(
                                        javaClass.name,
                                        "Stored StoryBookParagraph_Word in database. StoryBookParagraph_id: " + storyBookParagraph_Word.storyBookParagraph_id + ", words_id: " + storyBookParagraph_Word.words_id + ", words_ORDER: " + storyBookParagraph_Word.words_ORDER
                                    )
                                }
                            }
                        }
                    }
                }

                // Update the UI
                val storyBooks = storyBookDao.loadAll()
                Log.i(javaClass.name, "storyBooks.size(): " + storyBooks.size)
                activity!!.runOnUiThread {
                    textView!!.text = "storyBooks.size(): " + storyBooks.size
                    Snackbar.make(
                        textView!!,
                        "storyBooks.size(): " + storyBooks.size,
                        Snackbar.LENGTH_LONG
                    ).show()
                    progressBar!!.visibility = View.GONE
                }
            }
        })
    }
}
