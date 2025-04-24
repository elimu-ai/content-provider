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
        Log.i(javaClass.name, "onCreate")
        return true
    }

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
        val letterSoundDao = roomDb.letterSoundDao()

        val code = MATCHER.match(uri)
        Log.i(javaClass.name, "code: $code")
        if (code == CODE_LETTER_SOUNDS) {
            // Get the Room Cursor
            val cursor = letterSoundDao.loadAll_Cursor()
            Log.i(javaClass.name, "cursor: $cursor")

            cursor.setNotificationUri(context.contentResolver, uri)

            return cursor
        } else if (code == CODE_LETTER_SOUND_ID) {
            // Extract the LetterSound ID from the URI
            val pathSegments = uri.pathSegments
            Log.i(javaClass.name, "pathSegments: $pathSegments")
            val idAsString = pathSegments[1]
            val id = idAsString.toLong()
            Log.i(javaClass.name, "id: $id")

            // Get the Room Cursor
            val cursor = letterSoundDao.load_Cursor(id)
            Log.i(javaClass.name, "cursor: $cursor")

            cursor.setNotificationUri(context.contentResolver, uri)

            return cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        Log.i(javaClass.name, "getType")

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.i(javaClass.name, "insert")

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        Log.i(javaClass.name, "update")

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.i(javaClass.name, "delete")

        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        private const val AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.letter_sound_provider"
        private const val TABLE_LETTER_SOUNDS = "letter_sounds"
        private const val CODE_LETTER_SOUNDS = 1
        private const val CODE_LETTER_SOUND_ID = 2
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_LETTER_SOUNDS, CODE_LETTER_SOUNDS)
            MATCHER.addURI(AUTHORITY, TABLE_LETTER_SOUNDS + "/#", CODE_LETTER_SOUND_ID)
        }
    }
}
