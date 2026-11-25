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

/**
 * Provides the words (labels) of an image to other apps, based on the `Image_Word` table.
 *
 * The rows returned are [Word]s, so the `:utils` module can convert them with
 * `CursorToWordGsonConverter`.
 */
class ImageWordContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        Log.i(TAG, "onCreate")

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
        sortOrder: String?
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
        val imageWordDao = roomDb.image_WordDao()

        val code = MATCHER.match(uri)
        Log.i(TAG, "code: $code")
        when (code) {
            CODE_IMAGE_WORDS_BY_IMAGE_ID -> {
                // Extract the Image ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(TAG, "pathSegments: $pathSegments")
                val imageIdAsString = pathSegments[2]
                val imageId = imageIdAsString.toLong()
                Log.i(TAG, "imageId: $imageId")

                // Get the Room Cursor
                val cursor = imageWordDao.loadAllWordsAsCursor(imageId)
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
        Log.i(TAG, "prepareBundle")
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
        selectionArgs: Array<String>?
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
        private const val TAG = "ImageWordContentProvider"

        // The authority of this content provider
        private const val AUTHORITY: String = BuildConfig.APPLICATION_ID + ".provider.image_word_provider"

        private const val TABLE_IMAGE_WORDS = "image_words"
        private const val CODE_IMAGE_WORDS_BY_IMAGE_ID = 1

        // The URI matcher
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(
                AUTHORITY,
                "$TABLE_IMAGE_WORDS/by-image-id/#",
                CODE_IMAGE_WORDS_BY_IMAGE_ID
            )
        }
    }
}
