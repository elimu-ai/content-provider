package ai.elimu.content_provider.provider

import ai.elimu.content_provider.BuildConfig
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.content_provider.room.entity.Word
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri

class WordContentProvider : ContentProvider() {
    
    private val TAG = "WordContentProvider"
    
    override fun onCreate(): Boolean {
        Log.i(TAG, "onCreate")

        Log.i(TAG, "URI_WORD: $URI_WORD")

        return true
    }

    /**
     * Handles query requests from clients.
     */
    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?,
    ): Cursor? {
        Log.i(TAG, "query")

        Log.i(TAG, "uri: $uri")
        Log.i(TAG, "projection: $projection")
        Log.i(TAG, "selection: $selection")
        Log.i(TAG, "selectionArgs: $selectionArgs")
        Log.i(TAG, "sortOrder: $sortOrder")

        val context = context
        Log.i(TAG, "context: $context")
        if (context == null) {
            return null
        }

        val roomDb = RoomDb.getDatabase(context)
        val wordDao = roomDb.wordDao()

        val code = MATCHER.match(uri)
        Log.i(TAG, "code: $code")
        when (code) {
            CODE_WORDS -> {
                // Get the Room Cursor
                val cursor = wordDao.loadAllOrderedByUsageCountAsCursor()
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                cursor.extras = prepareBundle()

                return cursor
            }
            CODE_WORDS_BY_STORYBOOK_PARAGRAPH_ID -> {
                // Extract the StoryBookParagraph ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(TAG, "pathSegments: $pathSegments")
                val storyBookParagraphIdAsString = pathSegments[2]
                val storyBookParagraphId = storyBookParagraphIdAsString.toLong()
                Log.i(TAG, "storyBookParagraphId: $storyBookParagraphId")

                // Get the Room Cursor
                val cursor = wordDao.loadAllAsCursor(storyBookParagraphId)
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                cursor.extras = prepareBundle()

                return cursor
            }
            CODE_WORD_ID -> {
                // Extract the Word ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(TAG, "pathSegments: $pathSegments")
                val wordIdAsString = pathSegments[1]
                val wordId = wordIdAsString.toLong()
                Log.i(TAG, "wordId: $wordId")

                // Get the Room Cursor
                val cursor = wordDao.loadAsCursor(wordId)
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                cursor.extras = prepareBundle()

                return cursor
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    /**
     * Prepare database column names needed by the Cursor-to-Gson converter in the `:utils` module.
     */
    private fun prepareBundle(): Bundle {
        Log.i(this::class.simpleName, "prepareBundle")
        val bundle = Bundle().apply {
            putInt("version_code", BuildConfig.VERSION_CODE)
            putString("id", Word::id.name)
            putString("revision_number", Word::revisionNumber.name)
            putString("usage_count", Word::usageCount.name)
            putString("text", Word::text.name)
            putString("word_type", Word::wordType.name)
        }
        return bundle
    }

    /**
     * Handles requests for the MIME type of the data at the given URI.
     */
    override fun getType(uri: Uri): String? {
        Log.i(TAG, "getType")

        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handles requests to insert a new row.
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.i(TAG, "insert")

        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handles requests to update one or more rows.
     */
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?,
    ): Int {
        Log.i(TAG, "update")

        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handle requests to delete one or more rows.
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.i(TAG, "delete")

        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        // The authority of this content provider
        private const val AUTHORITY: String = BuildConfig.APPLICATION_ID + ".provider.word_provider"

        private const val TABLE_WORDS = "words"
        private const val CODE_WORDS = 1
        private const val CODE_WORD_ID = 2
        private const val CODE_WORDS_BY_STORYBOOK_PARAGRAPH_ID = 3
        val URI_WORD: Uri = ("content://$AUTHORITY/$TABLE_WORDS").toUri()

        // The URI matcher
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_WORDS, CODE_WORDS)
            MATCHER.addURI(AUTHORITY, "$TABLE_WORDS/#", CODE_WORD_ID)
            MATCHER.addURI(
                AUTHORITY,
                "$TABLE_WORDS/by-paragraph-id/#",
                CODE_WORDS_BY_STORYBOOK_PARAGRAPH_ID
            )
        }
    }
}
