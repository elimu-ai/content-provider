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
        Log.i(javaClass.getName(), "onCreate")
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String?>?,
        selection: String?,
        selectionArgs: Array<String?>?,
        sortOrder: String?
    ): Cursor? {
        Log.i(javaClass.getName(), "query")

        Log.i(javaClass.getName(), "uri: " + uri)
        Log.i(javaClass.getName(), "projection: " + projection)
        Log.i(javaClass.getName(), "selection: " + selection)
        Log.i(javaClass.getName(), "selectionArgs: " + selectionArgs)
        Log.i(javaClass.getName(), "sortOrder: " + sortOrder)

        val context = getContext()
        Log.i(javaClass.getName(), "context: " + context)
        if (context == null) {
            return null
        }

        val roomDb = RoomDb.getDatabase(context)
        val letterDao = roomDb.letterDao()

        val code: Int = MATCHER.match(uri)
        Log.i(javaClass.getName(), "code: " + code)
        if (code == CODE_LETTERS) {
            val cursor: Cursor

            // Get the Room Cursor
            cursor = letterDao.loadAllOrderedByUsageCount_Cursor()
            Log.i(javaClass.getName(), "cursor: " + cursor)

            cursor.setNotificationUri(context.getContentResolver(), uri)

            return cursor
        } else if (code == CODE_LETTERS_BY_LETTER_SOUND_ID) {
            // Extract the letter-sound correspondence ID from the URI
            val pathSegments = uri.getPathSegments()
            Log.i(javaClass.getName(), "pathSegments: " + pathSegments)
            val letterSoundIdAsString = pathSegments.get(2)
            val letterSoundId = letterSoundIdAsString.toLong()
            Log.i(javaClass.getName(), "letterSoundId: " + letterSoundId)

            val cursor: Cursor

            // Get the Room Cursor
            cursor = letterDao.loadAllByLetterSound(letterSoundId)
            Log.i(javaClass.getName(), "cursor: " + cursor)

            cursor.setNotificationUri(context.getContentResolver(), uri)

            return cursor
        } else if (code == CODE_LETTER_ID) {
            // Extract the Letter ID from the URI
            val pathSegments = uri.getPathSegments()
            Log.i(javaClass.getName(), "pathSegments: " + pathSegments)
            val letterIdAsString = pathSegments.get(1)
            val letterId = letterIdAsString.toLong()
            Log.i(javaClass.getName(), "letterId: " + letterId)

            val cursor: Cursor

            // Get the Room Cursor
            cursor = letterDao.load_Cursor(letterId)
            Log.i(javaClass.getName(), "cursor: " + cursor)

            cursor.setNotificationUri(context.getContentResolver(), uri)

            return cursor
        } else {
            throw IllegalArgumentException("Unknown URI: " + uri)
        }
    }

    override fun getType(uri: Uri): String? {
        Log.i(javaClass.getName(), "getType")

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?,
    ): Uri? {
        Log.i(javaClass.getName(), "insert")

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String?>?,
    ): Int {
        Log.i(javaClass.getName(), "delete")

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String?>?,
    ): Int {
        Log.i(javaClass.getName(), "update")

        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        private val AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.letter_provider"
        private const val TABLE_LETTERS = "letters"
        private const val CODE_LETTERS = 1
        private const val CODE_LETTER_ID = 2

        private const val CODE_LETTERS_BY_LETTER_SOUND_ID = 3
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_LETTERS, CODE_LETTERS)
            MATCHER.addURI(AUTHORITY, TABLE_LETTERS + "/#", CODE_LETTER_ID)
            MATCHER.addURI(
                AUTHORITY,
                TABLE_LETTERS + "/by-letter-sound-id/#",
                CODE_LETTERS_BY_LETTER_SOUND_ID
            )
        }
    }
}
