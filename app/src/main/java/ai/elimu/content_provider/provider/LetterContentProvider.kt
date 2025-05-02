package ai.elimu.content_provider.provider

import ai.elimu.content_provider.BuildConfig
import ai.elimu.content_provider.room.db.RoomDb
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log

class LetterContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        Log.i(TAG, "onCreate")
        return true
    }

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
        val letterDao = roomDb.letterDao()

        val code: Int = MATCHER.match(uri)
        Log.i(TAG, "code: $code")
        when (code) {
            CODE_LETTERS -> {

                // Get the Room Cursor
                val cursor: Cursor = letterDao.loadAllOrderedByUsageCountCursor()
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                return cursor
            }
            CODE_LETTERS_BY_LETTER_SOUND_ID -> {
                // Extract the letter-sound correspondence ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(TAG, "pathSegments: $pathSegments")
                val letterSoundIdAsString = pathSegments[2]
                val letterSoundId = letterSoundIdAsString.toLong()
                Log.i(TAG, "letterSoundId: $letterSoundId")

                // Get the Room Cursor
                val cursor: Cursor = letterDao.loadAllByLetterSound(letterSoundId)
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                return cursor
            }
            CODE_LETTER_ID -> {
                // Extract the Letter ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(TAG, "pathSegments: $pathSegments")
                val letterIdAsString = pathSegments[1]
                val letterId = letterIdAsString.toLong()
                Log.i(TAG, "letterId: $letterId")

                // Get the Room Cursor
                val cursor: Cursor = letterDao.loadCursor(letterId)
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

    override fun insert(
        uri: Uri,
        values: ContentValues?,
    ): Uri? {
        Log.i(TAG, "insert")

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String?>?,
    ): Int {
        Log.i(TAG, "delete")

        throw UnsupportedOperationException("Not yet implemented")
    }

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
        private const val TAG = "LetterContentProvider"
        private const val AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.letter_provider"
        private const val TABLE_LETTERS = "letters"
        private const val CODE_LETTERS = 1
        private const val CODE_LETTER_ID = 2

        private const val CODE_LETTERS_BY_LETTER_SOUND_ID = 3
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_LETTERS, CODE_LETTERS)
            MATCHER.addURI(AUTHORITY, "$TABLE_LETTERS/#", CODE_LETTER_ID)
            MATCHER.addURI(
                AUTHORITY,
                "$TABLE_LETTERS/by-letter-sound-id/#",
                CODE_LETTERS_BY_LETTER_SOUND_ID
            )
        }
    }
}
