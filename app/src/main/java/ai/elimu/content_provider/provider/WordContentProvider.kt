package ai.elimu.content_provider.provider

import ai.elimu.content_provider.BuildConfig
import ai.elimu.content_provider.room.db.RoomDb
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log

class WordContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        Log.i(javaClass.name, "onCreate")

        Log.i(javaClass.name, "URI_WORD: " + URI_WORD)

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
        Log.i(javaClass.name, "query")

        Log.i(javaClass.name, "uri: $uri")
        Log.i(javaClass.name, "projection: $projection")
        Log.i(javaClass.name, "selection: $selection")
        Log.i(javaClass.name, "selectionArgs: $selectionArgs")
        Log.i(javaClass.name, "sortOrder: $sortOrder")

        val context = context
        Log.i(javaClass.name, "context: $context")
        if (context == null) {
            return null
        }

        val roomDb = RoomDb.getDatabase(context)
        val wordDao = roomDb.wordDao()

        val code = MATCHER.match(uri)
        Log.i(javaClass.name, "code: $code")
        if (code == CODE_WORDS) {
            // Get the Room Cursor
            val cursor = wordDao.loadAllOrderedByUsageCountAsCursor()
            Log.i(javaClass.name, "cursor: $cursor")

            cursor.setNotificationUri(context.contentResolver, uri)

            return cursor
        } else if (code == CODE_WORDS_BY_STORYBOOK_PARAGRAPH_ID) {
            // Extract the StoryBookParagraph ID from the URI
            val pathSegments = uri.pathSegments
            Log.i(javaClass.name, "pathSegments: $pathSegments")
            val storyBookParagraphIdAsString = pathSegments[2]
            val storyBookParagraphId = storyBookParagraphIdAsString.toLong()
            Log.i(javaClass.name, "storyBookParagraphId: $storyBookParagraphId")

            // Get the Room Cursor
            val cursor = wordDao.loadAllAsCursor(storyBookParagraphId)
            Log.i(javaClass.name, "cursor: $cursor")

            cursor.setNotificationUri(context.contentResolver, uri)

            return cursor
        } else if (code == CODE_WORD_ID) {
            // Extract the Word ID from the URI
            val pathSegments = uri.pathSegments
            Log.i(javaClass.name, "pathSegments: $pathSegments")
            val wordIdAsString = pathSegments[1]
            val wordId = wordIdAsString.toLong()
            Log.i(javaClass.name, "wordId: $wordId")

            // Get the Room Cursor
            val cursor = wordDao.loadAsCursor(wordId)
            Log.i(javaClass.name, "cursor: $cursor")

            cursor.setNotificationUri(context.contentResolver, uri)

            return cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    /**
     * Handles requests for the MIME type of the data at the given URI.
     */
    override fun getType(uri: Uri): String? {
        Log.i(javaClass.name, "getType")

        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handles requests to insert a new row.
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.i(javaClass.name, "insert")

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
        Log.i(javaClass.name, "update")

        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handle requests to delete one or more rows.
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.i(javaClass.name, "delete")

        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        // The authority of this content provider
        const val AUTHORITY: String = BuildConfig.APPLICATION_ID + ".provider.word_provider"

        private const val TABLE_WORDS = "words"
        private const val CODE_WORDS = 1
        private const val CODE_WORD_ID = 2
        private const val CODE_WORDS_BY_STORYBOOK_PARAGRAPH_ID = 3
        val URI_WORD: Uri = Uri.parse("content://" + AUTHORITY + "/" + TABLE_WORDS)

        // The URI matcher
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_WORDS, CODE_WORDS)
            MATCHER.addURI(AUTHORITY, TABLE_WORDS + "/#", CODE_WORD_ID)
            MATCHER.addURI(
                AUTHORITY,
                TABLE_WORDS + "/by-paragraph-id/#",
                CODE_WORDS_BY_STORYBOOK_PARAGRAPH_ID
            )
        }
    }
}
