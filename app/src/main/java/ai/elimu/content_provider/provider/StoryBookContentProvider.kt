package ai.elimu.content_provider.provider

import ai.elimu.content_provider.BuildConfig
import ai.elimu.content_provider.room.db.RoomDb
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log

class StoryBookContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        Log.i(javaClass.name, "onCreate")

        Log.i(javaClass.name, "URI_STORYBOOK: " + URI_STORYBOOK)

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

        val code = MATCHER.match(uri)
        Log.i(javaClass.name, "code: $code")
        if (code == CODE_STORYBOOKS) {
            val cursor: Cursor

            // Get the Room Cursor
            val storyBookDao = roomDb.storyBookDao()
            cursor = storyBookDao.loadAllAsCursor()
            Log.i(javaClass.name, "cursor: $cursor")

            cursor.setNotificationUri(context.contentResolver, uri)

            return cursor
        } else if (code == CODE_STORYBOOK_ID) {
            // Extract the StoryBook ID from the URI
            val pathSegments = uri.pathSegments
            Log.i(javaClass.name, "pathSegments: $pathSegments")
            val storyBookIdAsString = pathSegments[1]
            val storyBookId = storyBookIdAsString.toLong()
            Log.i(javaClass.name, "storyBookId: $storyBookId")

            val cursor: Cursor

            // Get the Room Cursor
            val storyBookDao = roomDb.storyBookDao()
            cursor = storyBookDao.loadAsCursor(storyBookId)
            Log.i(javaClass.name, "cursor: $cursor")

            cursor.setNotificationUri(context.contentResolver, uri)

            return cursor
        } else if (code == CODE_STORYBOOK_CHAPTERS) {
            // Extract the StoryBook ID from the URI
            val pathSegments = uri.pathSegments
            Log.i(javaClass.name, "pathSegments: $pathSegments")
            val storyBookIdAsString = pathSegments[1]
            val storyBookId = storyBookIdAsString.toLong()
            Log.i(javaClass.name, "storyBookId: $storyBookId")

            val cursor: Cursor

            // Get the Room Cursor
            val storyBookChapterDao = roomDb.storyBookChapterDao()
            cursor = storyBookChapterDao.loadAllAsCursor(storyBookId)
            Log.i(javaClass.name, "cursor: $cursor")

            cursor.setNotificationUri(context.contentResolver, uri)

            return cursor
        } else if (code == CODE_STORYBOOK_CHAPTER_PARAGRAPHS) {
            // Extract the StoryBookChapter ID from the URI
            val pathSegments = uri.pathSegments
            val storyBookChapterIdAsString = pathSegments[3]
            val storyBookChapterId = storyBookChapterIdAsString.toLong()
            Log.i(javaClass.name, "storyBookChapterId: $storyBookChapterId")

            val cursor: Cursor

            // Get the Room Cursor
            val storyBookParagraphDao = roomDb.storyBookParagraphDao()
            cursor = storyBookParagraphDao.loadAllAsCursor(storyBookChapterId)
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
        const val AUTHORITY: String = BuildConfig.APPLICATION_ID + ".provider.storybook_provider"

        private const val TABLE_STORYBOOKS = "storybooks"
        private const val CODE_STORYBOOKS = 1
        private const val CODE_STORYBOOK_ID = 2
        private const val CODE_STORYBOOK_CHAPTERS = 3
        private const val CODE_STORYBOOK_CHAPTER_PARAGRAPHS = 4
        val URI_STORYBOOK: Uri = Uri.parse("content://" + AUTHORITY + "/" + TABLE_STORYBOOKS)

        // The URI matcher
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_STORYBOOKS, CODE_STORYBOOKS)
            MATCHER.addURI(AUTHORITY, TABLE_STORYBOOKS + "/#", CODE_STORYBOOK_ID)
            MATCHER.addURI(AUTHORITY, TABLE_STORYBOOKS + "/#/chapters", CODE_STORYBOOK_CHAPTERS)
            MATCHER.addURI(
                AUTHORITY,
                TABLE_STORYBOOKS + "/#/chapters/#/paragraphs",
                CODE_STORYBOOK_CHAPTER_PARAGRAPHS
            )
        }
    }
}
