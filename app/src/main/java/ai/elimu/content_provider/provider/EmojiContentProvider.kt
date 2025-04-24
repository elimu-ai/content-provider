package ai.elimu.content_provider.provider

import ai.elimu.content_provider.BuildConfig
import ai.elimu.content_provider.room.db.RoomDb
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
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

        Log.i(TAG, "uri: " + uri)
        Log.i(TAG, "projection: " + projection)
        Log.i(TAG, "selection: " + selection)
        Log.i(TAG, "selectionArgs: " + selectionArgs)
        Log.i(TAG, "sortOrder: " + sortOrder)

        val context = getContext()
        Log.i(TAG, "context: " + context)
        if (context == null) {
            return null
        }

        val roomDb = RoomDb.getDatabase(context)
        val emojiDao = roomDb.emojiDao()

        val code: Int = MATCHER.match(uri)
        Log.i(TAG, "code: " + code)
        if (code == CODE_EMOJIS) {
            val cursor: Cursor

            // Get the Room Cursor
            cursor = emojiDao.loadAllAsCursor()
            Log.i(TAG, "cursor: " + cursor)

            cursor.setNotificationUri(context.getContentResolver(), uri)

            return cursor
        } else if (code == CODE_EMOJI_ID) {
            // Extract the Emoji ID from the URI
            val pathSegments = uri.getPathSegments()
            Log.i(TAG, "pathSegments: " + pathSegments)
            val emojiIdAsString = pathSegments.get(1)
            val emojiId = emojiIdAsString.toLong()
            Log.i(TAG, "emojiId: " + emojiId)

            val cursor: Cursor

            // Get the Room Cursor
            cursor = emojiDao.loadAsCursor(emojiId)
            Log.i(TAG, "cursor: " + cursor)

            cursor.setNotificationUri(context.getContentResolver(), uri)

            return cursor
        } else if (code == CODE_EMOJIS_BY_WORD_LABEL_ID) {
            // Extract the Word ID from the URI
            val pathSegments = uri.getPathSegments()
            Log.i(TAG, "pathSegments: " + pathSegments)
            val wordIdAsString = pathSegments.get(2)
            val wordId = wordIdAsString.toLong()
            Log.i(TAG, "wordId: " + wordId)

            val cursor: Cursor

            // Get the Room Cursor
            cursor = emojiDao.loadAllByWordLabelAsCursor(wordId)
            Log.i(TAG, "cursor: " + cursor)

            cursor.setNotificationUri(context.getContentResolver(), uri)

            return cursor
        } else {
            throw IllegalArgumentException("Unknown URI: " + uri)
        }
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
        val AUTHORITY: String = BuildConfig.APPLICATION_ID + ".provider.emoji_provider"

        private const val TABLE_EMOJIS = "emojis"
        private const val CODE_EMOJIS = 1
        private const val CODE_EMOJI_ID = 2
        private const val CODE_EMOJIS_BY_WORD_LABEL_ID = 3

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_EMOJIS, CODE_EMOJIS)
            MATCHER.addURI(AUTHORITY, TABLE_EMOJIS + "/#", CODE_EMOJI_ID)
            MATCHER.addURI(
                AUTHORITY,
                TABLE_EMOJIS + "/by-word-label-id/#",
                CODE_EMOJIS_BY_WORD_LABEL_ID
            )
        }
    }
}
