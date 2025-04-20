package ai.elimu.content_provider.ui.storybook

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.databinding.FragmentStorybooksBinding
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class StoryBooksFragment : Fragment() {

    private val TAG = javaClass.name
    private lateinit var storyBooksViewModel: StoryBooksViewModel
    private lateinit var binding: FragmentStorybooksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")

        storyBooksViewModel = ViewModelProvider(this)[StoryBooksViewModel::class.java]
        binding = FragmentStorybooksBinding.inflate(layoutInflater)
        storyBooksViewModel.text.observe(viewLifecycleOwner) { s ->
            Log.i(TAG, "onChanged")
            binding.textStorybooks.text = s
        }
        return binding.root
    }

    override fun onStart() {
        Log.i(TAG, "onStart")
        super.onStart()

        // Download StoryBooks from REST API, and store them in the database
        val baseApplication = activity?.application as? BaseApplication ?: return
        val retrofit = baseApplication.retrofit
        val storyBooksService = retrofit.create(
            StoryBooksService::class.java
        )
        val call = storyBooksService.listStoryBooks()
        Log.i(TAG, "call.request(): " + call.request())
        call.enqueue(object : Callback<List<StoryBookGson>> {
            override fun onResponse(
                call: Call<List<StoryBookGson>>,
                response: Response<List<StoryBookGson>>
            ) {
                Log.i(TAG, "onResponse")

                Log.i(TAG, "response: $response")
                if (response.isSuccessful) {
                    val storyBookGsons = response.body() ?: return
                    Log.i(TAG, "storyBookGsons.size(): " + storyBookGsons.size)

                    if (storyBookGsons.size > 0) {
                        processResponseBody(storyBookGsons)
                    }
                } else {
                    // Handle error
                    Snackbar.make(binding.textStorybooks, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    binding.progressBarStorybooks.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<StoryBookGson>>, t: Throwable) {
                Log.e(TAG, "onFailure", t)

                Log.e(TAG, "t.getCause():", t.cause)

                // Handle error
                Snackbar.make(binding.textStorybooks, t.cause.toString(), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                    .show()
                binding.progressBarStorybooks.visibility = View.GONE
            }
        })
    }

    private fun processResponseBody(storyBookGsons: List<StoryBookGson>) {
        Log.i(TAG, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute {
            Log.i(TAG, "run")

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
                Log.i(TAG, "storyBookGson.getId(): " + storyBookGson.id)

                // Store the StoryBook in the database
                val storyBook = getStoryBook(storyBookGson)
                storyBook?.let {
                    storyBookDao.insert(storyBook)
                    Log.i(TAG, "Stored StoryBook in database with ID " + storyBook.id)
                }

                val storyBookChapterGsons = storyBookGson.storyBookChapters
                Log.i(
                    TAG,
                    "storyBookChapterGsons.size(): " + storyBookChapterGsons.size
                )
                for (storyBookChapterGson in storyBookChapterGsons) {
                    val storyBookChapter = getStoryBookChapter(storyBookChapterGson)
                    storyBookChapter?.let {
                        storyBookChapter.storyBookId = storyBookGson.id
                        storyBookChapterDao.insert(storyBookChapter)
                        Log.i(
                            TAG,
                            "Stored StoryBookChapter in database with ID " + storyBookChapter.id
                        )
                    }

                    val storyBookParagraphs = storyBookChapterGson.storyBookParagraphs
                    Log.i(
                        TAG,
                        "storyBookParagraphs.size(): " + storyBookParagraphs.size
                    )
                    for (storyBookParagraphGson in storyBookParagraphs) {
                        val storyBookParagraph = getStoryBookParagraph(storyBookParagraphGson)
                        storyBookParagraph?.let {
                            storyBookParagraph.storyBookChapterId = storyBookChapterGson.id
                            storyBookParagraphDao.insert(storyBookParagraph)
                            Log.i(
                                TAG,
                                "Stored StoryBookParagraph in database with ID " + storyBookParagraph.id
                            )
                        }

                        // Store all the StoryBookParagraph's Words in the database
                        val wordGsons = storyBookParagraphGson.words
                        Log.i(TAG, "wordGsons.size(): " + wordGsons.size)
                        for (i in wordGsons.indices) {
                            val wordGson = wordGsons[i]
                            Log.i(TAG, "wordGson: $wordGson")
                            if (wordGson != null) {
                                Log.i(TAG, "wordGson.getId(): " + wordGson.id)
                                val storyBookParagraph_Word = StoryBookParagraph_Word()
                                storyBookParagraph_Word.storyBookParagraph_id =
                                    storyBookParagraphGson.id
                                storyBookParagraph_Word.words_id = wordGson.id
                                storyBookParagraph_Word.words_ORDER = i
                                storyBookParagraph_WordDao.insert(storyBookParagraph_Word)
                                Log.i(
                                    TAG,
                                    "Stored StoryBookParagraph_Word in database. StoryBookParagraph_id: " + storyBookParagraph_Word.storyBookParagraph_id + ", words_id: " + storyBookParagraph_Word.words_id + ", words_ORDER: " + storyBookParagraph_Word.words_ORDER
                                )
                            }
                        }
                    }
                }
            }

            // Update the UI
            val storyBooks = storyBookDao.loadAll()
            Log.i(TAG, "storyBooks.size(): " + storyBooks.size)
            activity?.runOnUiThread {
                binding.textStorybooks.text = "storyBooks.size(): " + storyBooks.size
                Snackbar.make(
                    binding.textStorybooks,
                    "storyBooks.size(): " + storyBooks.size,
                    Snackbar.LENGTH_LONG
                ).show()
                binding.progressBarStorybooks.visibility = View.GONE
            }
        }
    }
}
