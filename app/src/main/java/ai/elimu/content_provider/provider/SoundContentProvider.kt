package ai.elimu.content_provider.provider

import ai.elimu.content_provider.BuildConfig
import ai.elimu.content_provider.room.db.RoomDb
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log

class SoundContentProvider : ContentProvider() {
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
        val soundDao = roomDb.soundDao()

        val code = MATCHER.match(uri)
        Log.i(javaClass.name, "code: $code")
        if (code == CODE_SOUNDS) {
            // Get the Room Cursor
            val cursor = soundDao.loadAllOrderedByUsageCount_Cursor()
            Log.i(javaClass.name, "cursor: $cursor")

            cursor.setNotificationUri(context.contentResolver, uri)

            return cursor
        } else if (code == CODE_SOUNDS_BY_LETTER_SOUND_ID) {
            // Extract the letter-sound correspondence ID from the URI
            val pathSegments = uri.pathSegments
            Log.i(javaClass.name, "pathSegments: $pathSegments")
            val letterSoundIdAsString = pathSegments[2]
            val letterSoundId = letterSoundIdAsString.toLong()
            Log.i(javaClass.name, "letterSoundId: $letterSoundId")

            // Get the Room Cursor
            val cursor = soundDao.loadAllByLetterSound(letterSoundId)
            Log.i(javaClass.name, "cursor: $cursor")

            cursor.setNotificationUri(context.contentResolver, uri)

            return cursor
        } else if (code == CODE_SOUND_ID) {
            // Extract the Sound ID from the URI
            val pathSegments = uri.pathSegments
            Log.i(javaClass.name, "pathSegments: $pathSegments")
            val soundIdAsString = pathSegments[1]
            val soundId = soundIdAsString.toLong()
            Log.i(javaClass.name, "soundId: $soundId")

            // Get the Room Cursor
            val cursor = soundDao.load_Cursor(soundId)
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
        private const val AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.sound_provider"
        private const val TABLE_SOUNDS = "sounds"
        private const val CODE_SOUNDS = 1
        private const val CODE_SOUND_ID = 2

        private const val CODE_SOUNDS_BY_LETTER_SOUND_ID = 3
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_SOUNDS, CODE_SOUNDS)
            MATCHER.addURI(AUTHORITY, TABLE_SOUNDS + "/#", CODE_SOUND_ID)
            MATCHER.addURI(
                AUTHORITY,
                TABLE_SOUNDS + "/by-letter-sound-id/#",
                CODE_SOUNDS_BY_LETTER_SOUND_ID
            )
        }
    }
}
