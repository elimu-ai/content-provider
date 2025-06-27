package ai.elimu.content_provider.provider

import ai.elimu.content_provider.BuildConfig
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.content_provider.room.entity.Emoji
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log

class EmojiContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        Log.i(TAG, "onCreate")

        return true
    }

    /**
     * Handles query requests from clients.
     */
    override fun query(
        uri: Uri,
        projection: Array<String?>?,
        selection: String?,
        selectionArgs: Array<String?>?,
        sortOrder: String?
    ): Cursor? {
        Log.i(TAG, "query")

        Log.i(TAG, "uri: $uri")
        Log.i(TAG, "projection: $projection")
        Log.i(TAG, "selection: $selection")
        Log.i(TAG, "selectionArgs: $selectionArgs")
        Log.i(TAG, "sortOrder: $sortOrder")

        val context = context ?: return null
        Log.i(TAG, "context: $context")

        val roomDb = RoomDb.getDatabase(context)
        val emojiDao = roomDb.emojiDao()

        val code: Int = MATCHER.match(uri)
        Log.i(TAG, "code: $code")
        when (code) {
            CODE_EMOJIS -> {

                // Get the Room Cursor
                val cursor: Cursor = emojiDao.loadAllAsCursor()
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                cursor.extras = prepareBundle()

                return cursor
            }
            CODE_EMOJI_ID -> {
                // Extract the Emoji ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(TAG, "pathSegments: $pathSegments")
                val emojiIdAsString = pathSegments[1]
                val emojiId = emojiIdAsString.toLong()
                Log.i(TAG, "emojiId: $emojiId")

                // Get the Room Cursor
                val cursor: Cursor = emojiDao.loadAsCursor(emojiId)
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                cursor.extras = prepareBundle()

                return cursor
            }
            CODE_EMOJIS_BY_WORD_LABEL_ID -> {
                // Extract the Word ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(TAG, "pathSegments: $pathSegments")
                val wordIdAsString = pathSegments[2]
                val wordId = wordIdAsString.toLong()
                Log.i(TAG, "wordId: $wordId")

                // Get the Room Cursor
                val cursor: Cursor = emojiDao.loadAllByWordLabelAsCursor(wordId)
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
            putString("id", Emoji::id.name)
            putString("revision_number", Emoji::revisionNumber.name)
            putString("usage_count", Emoji::usageCount.name)
            putString("glyph", Emoji::glyph.name)
            putString("unicode_version", Emoji::unicodeVersion.name)
            putString("unicode_emoji_version", Emoji::unicodeEmojiVersion.name)
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
    override fun insert(
        uri: Uri,
        values: ContentValues?,
    ): Uri? {
        Log.i(TAG, "insert")

        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handle requests to delete one or more rows.
     */
    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String?>?,
    ): Int {
        Log.i(TAG, "delete")

        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handles requests to update one or more rows.
     */
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String?>?,
    ): Int {
        Log.i(TAG, "update")

        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        private const val TAG = "EmojiContentProvider"
        private const val AUTHORITY: String = BuildConfig.APPLICATION_ID + ".provider.emoji_provider"

        private const val TABLE_EMOJIS = "emojis"
        private const val CODE_EMOJIS = 1
        private const val CODE_EMOJI_ID = 2
        private const val CODE_EMOJIS_BY_WORD_LABEL_ID = 3

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_EMOJIS, CODE_EMOJIS)
            MATCHER.addURI(AUTHORITY, "$TABLE_EMOJIS/#", CODE_EMOJI_ID)
            MATCHER.addURI(
                AUTHORITY,
                "$TABLE_EMOJIS/by-word-label-id/#",
                CODE_EMOJIS_BY_WORD_LABEL_ID
            )
        }
    }
}
