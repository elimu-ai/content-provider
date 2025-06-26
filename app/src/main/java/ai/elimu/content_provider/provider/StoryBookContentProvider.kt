package ai.elimu.content_provider.provider

import ai.elimu.content_provider.BuildConfig
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.content_provider.room.entity.StoryBook
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri

class StoryBookContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        Log.i(TAG, "onCreate")

        Log.i(TAG, "URI_STORYBOOK: $URI_STORYBOOK")

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

        val code = MATCHER.match(uri)
        Log.i(TAG, "code: $code")
        when (code) {
            CODE_STORYBOOKS -> {
                val cursor: Cursor

                // Get the Room Cursor
                val storyBookDao = roomDb.storyBookDao()
                cursor = storyBookDao.loadAllAsCursor()
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                cursor.extras = prepareBundle()

                return cursor
            }
            CODE_STORYBOOK_ID -> {
                // Extract the StoryBook ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(TAG, "pathSegments: $pathSegments")
                val storyBookIdAsString = pathSegments[1]
                val storyBookId = storyBookIdAsString.toLong()
                Log.i(TAG, "storyBookId: $storyBookId")

                val cursor: Cursor

                // Get the Room Cursor
                val storyBookDao = roomDb.storyBookDao()
                cursor = storyBookDao.loadAsCursor(storyBookId)
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                cursor.extras = prepareBundle()

                return cursor
            }
            CODE_STORYBOOK_CHAPTERS -> {
                // Extract the StoryBook ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(TAG, "pathSegments: $pathSegments")
                val storyBookIdAsString = pathSegments[1]
                val storyBookId = storyBookIdAsString.toLong()
                Log.i(TAG, "storyBookId: $storyBookId")

                val cursor: Cursor

                // Get the Room Cursor
                val storyBookChapterDao = roomDb.storyBookChapterDao()
                cursor = storyBookChapterDao.loadAllAsCursor(storyBookId)
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                cursor.extras = prepareBundle()

                return cursor
            }
            CODE_STORYBOOK_CHAPTER_PARAGRAPHS -> {
                // Extract the StoryBookChapter ID from the URI
                val pathSegments = uri.pathSegments
                val storyBookChapterIdAsString = pathSegments[3]
                val storyBookChapterId = storyBookChapterIdAsString.toLong()
                Log.i(TAG, "storyBookChapterId: $storyBookChapterId")

                val cursor: Cursor

                // Get the Room Cursor
                val storyBookParagraphDao = roomDb.storyBookParagraphDao()
                cursor = storyBookParagraphDao.loadAllAsCursor(storyBookChapterId)
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
            putString("id", StoryBook::id.name)
            putString("revision_number", StoryBook::revisionNumber.name)
            putString("usage_count", StoryBook::usageCount.name)
            putString("title", StoryBook::title.name)
            putString("description", StoryBook::description.name)
            putString("cover_image_id", StoryBook::coverImageId.name)
            putString("reading_level", StoryBook::readingLevel.name)
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
        private const val TAG = "StoryBookContentProvider"
        // The authority of this content provider
        private const val AUTHORITY: String = BuildConfig.APPLICATION_ID + ".provider.storybook_provider"

        private const val TABLE_STORYBOOKS = "storybooks"
        private const val CODE_STORYBOOKS = 1
        private const val CODE_STORYBOOK_ID = 2
        private const val CODE_STORYBOOK_CHAPTERS = 3
        private const val CODE_STORYBOOK_CHAPTER_PARAGRAPHS = 4
        val URI_STORYBOOK: Uri = ("content://$AUTHORITY/$TABLE_STORYBOOKS").toUri()

        // The URI matcher
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_STORYBOOKS, CODE_STORYBOOKS)
            MATCHER.addURI(AUTHORITY, "$TABLE_STORYBOOKS/#", CODE_STORYBOOK_ID)
            MATCHER.addURI(AUTHORITY, "$TABLE_STORYBOOKS/#/chapters", CODE_STORYBOOK_CHAPTERS)
            MATCHER.addURI(
                AUTHORITY,
                "$TABLE_STORYBOOKS/#/chapters/#/paragraphs",
                CODE_STORYBOOK_CHAPTER_PARAGRAPHS
            )
        }
    }
}
