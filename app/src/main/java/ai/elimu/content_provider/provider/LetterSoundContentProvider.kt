package ai.elimu.content_provider.provider

import ai.elimu.content_provider.BuildConfig
import ai.elimu.content_provider.room.db.RoomDb
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log

class LetterSoundContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        Log.i(TAG, "onCreate")
        return true
    }

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
        val letterSoundDao = roomDb.letterSoundDao()

        val code = MATCHER.match(uri)
        Log.i(TAG, "code: $code")
        when (code) {
            CODE_LETTER_SOUNDS -> {
                // Get the Room Cursor
                val cursor = letterSoundDao.loadAllCursor()
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                return cursor
            }
            CODE_LETTER_SOUND_ID -> {
                // Extract the LetterSound ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(TAG, "pathSegments: $pathSegments")
                val idAsString = pathSegments[1]
                val id = idAsString.toLong()
                Log.i(TAG, "id: $id")

                // Get the Room Cursor
                val cursor = letterSoundDao.loadCursor(id)
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                return cursor
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun getType(uri: Uri): String? {
        Log.i(TAG, "getType")

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.i(TAG, "insert")

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        Log.i(TAG, "update")

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.i(TAG, "delete")

        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        private const val TAG = "LetterSoundContentProvider"
        private const val AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.letter_sound_provider"
        private const val TABLE_LETTER_SOUNDS = "letter_sounds"
        private const val CODE_LETTER_SOUNDS = 1
        private const val CODE_LETTER_SOUND_ID = 2
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_LETTER_SOUNDS, CODE_LETTER_SOUNDS)
            MATCHER.addURI(AUTHORITY, "$TABLE_LETTER_SOUNDS/#", CODE_LETTER_SOUND_ID)
        }
    }
}
