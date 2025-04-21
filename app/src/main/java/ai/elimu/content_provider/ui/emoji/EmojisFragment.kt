package ai.elimu.content_provider.ui.emoji

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.databinding.FragmentEmojisBinding
import ai.elimu.content_provider.rest.EmojisService
import ai.elimu.content_provider.room.GsonToRoomConverter.getEmoji
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.content_provider.room.entity.Emoji_Word
import ai.elimu.model.v2.gson.content.EmojiGson
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class EmojisFragment : Fragment() {

    private val TAG = javaClass.name
    private var emojisViewModel: EmojisViewModel? = null
    private lateinit var binding: FragmentEmojisBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")

        emojisViewModel = ViewModelProvider(this)[EmojisViewModel::class.java]
        binding = FragmentEmojisBinding.inflate(layoutInflater)
        emojisViewModel?.getText()?.observe(viewLifecycleOwner) { s ->
            Log.i(TAG, "onChanged")
            binding.textEmojis.text = s
        }
        return binding.root
    }

    override fun onStart() {
        Log.i(TAG, "onStart")
        super.onStart()

        // Download Emojis from REST API, and store them in the database
        val baseApplication = activity?.application as? BaseApplication ?: return
        val retrofit = baseApplication.retrofit
        val emojisService = retrofit.create(EmojisService::class.java)
        val emojiGsonsCall = emojisService.listEmojis()
        Log.i(TAG, "emojiGsonsCall.request(): " + emojiGsonsCall.request())
        emojiGsonsCall.enqueue(object : Callback<List<EmojiGson>> {
            override fun onResponse(
                call: Call<List<EmojiGson>>,
                response: Response<List<EmojiGson>>
            ) {
                Log.i(TAG, "onResponse")

                Log.i(TAG, "response: $response")
                if (response.isSuccessful) {
                    val emojiGsons = response.body() ?: return
                    Log.i(TAG, "emojiGsons.size(): " + emojiGsons.size)

                    if (emojiGsons.isNotEmpty()) {
                        processResponseBody(emojiGsons)
                    }
                } else {
                    context?.let { context ->
                        // Handle error
                        Snackbar.make(binding.textEmojis, response.toString(), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(ContextCompat.getColor(context, R.color.deep_orange_darken_4))
                            .show()
                    }
                    binding.progressBarEmojis.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<EmojiGson>>, t: Throwable) {
                Log.e(TAG, "onFailure", t)

                Log.e(TAG, "t.getCause():", t.cause)

                context?.let { context ->
                    // Handle error
                    Snackbar.make(binding.textEmojis, t.cause.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(context, R.color.deep_orange_darken_4))
                        .show()
                }
                binding.progressBarEmojis.visibility = View.GONE
            }
        })
    }

    private fun processResponseBody(emojiGsons: List<EmojiGson>) {
        Log.i(TAG, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute {
            Log.i(TAG, "run")

            val roomDb = RoomDb.getDatabase(context)
            val emojiDao = roomDb.emojiDao()
            val emojiWordDao = roomDb.emoji_WordDao()

            // Empty the database table before downloading up-to-date content
            emojiWordDao.deleteAll()
            emojiDao.deleteAll()

            for (emojiGson in emojiGsons) {
                Log.i(TAG, "emojiGson.getId(): " + emojiGson.id)

                // Store the Emoji in the database
                val emoji = getEmoji(emojiGson)
                emoji?.let {
                    emojiDao.insert(emoji)
                    Log.i(TAG, "Stored Emoji in database with ID " + emoji.id)
                }

                // Store all the Emoji's Word labels in the database
                val wordGsons = emojiGson.words
                Log.i(TAG, "wordGsons.size(): " + wordGsons.size)
                for (wordGson in wordGsons) {
                    Log.i(TAG, "wordGson.getId(): " + wordGson.id)
                    val emojiWord = Emoji_Word()
                    emojiWord.emoji_id = emojiGson.id
                    emojiWord.words_id = wordGson.id
                    emojiWordDao.insert(emojiWord)
                    Log.i(
                        TAG,
                        "Stored Emoji_Word in database. Emoji_id: " + emojiWord.emoji_id + ", words_id: " + emojiWord.words_id
                    )
                }
            }

            // Update the UI
            val emojis = emojiDao.loadAll()
            Log.i(TAG, "emojis.size(): " + emojis.size)
            activity?.runOnUiThread {
                binding.textEmojis.text = getString(R.string.emojis_size, emojis.size)
                Snackbar.make(
                    binding.textEmojis,
                    "emojis.size(): " + emojis.size,
                    Snackbar.LENGTH_LONG
                )
                    .show()
                binding.progressBarEmojis.visibility = View.GONE
            }
        }
    }
}
