package ai.elimu.content_provider.utils.converter

import ai.elimu.content_provider.utils.converter.CursorToLetterGsonConverter.getLetterGson
import ai.elimu.content_provider.utils.converter.CursorToSoundGsonConverter.getSoundGson
import ai.elimu.model.v2.gson.content.LetterGson
import ai.elimu.model.v2.gson.content.LetterSoundGson
import ai.elimu.model.v2.gson.content.SoundGson
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CursorToLetterSoundGsonConverter {

    private const val TAG = "CursorToLetterSoundGsonConverter"
    private val mainScope by lazy { CoroutineScope(Dispatchers.Main) }

    fun getLetterSoundGson(
        cursor: Cursor,
        context: Context,
        contentProviderApplicationId: String
    ): LetterSoundGson {
        Log.i(TAG, "getLetterSoundGson")

        Log.i(TAG,
            "Arrays.toString(cursor.getColumnNames()): " + cursor.columnNames.contentToString())

        val columnIndexId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnIndexId)
        Log.i(TAG, "id: $id")

        val columnIndexRevisionNumber = cursor.getColumnIndex("revisionNumber")
        val revisionNumber = cursor.getInt(columnIndexRevisionNumber)
        Log.i(TAG, "revisionNumber: $revisionNumber")

        val columnIndexUsageCount = cursor.getColumnIndex("usageCount")
        val usageCount = cursor.getInt(columnIndexUsageCount)
        Log.i(TAG, "usageCount: $usageCount")

        val letterGsons = mutableListOf<LetterGson>()
        val lettersUri =
            Uri.parse("content://$contentProviderApplicationId.provider.letter_provider/letters/by-letter-sound-id/$id")
        Log.i(TAG, "lettersUri: $lettersUri")
        val lettersCursor = context.contentResolver.query(lettersUri, null, null, null, null)
        if (lettersCursor == null) {
            Log.e(TAG, "lettersCursor == null")
            mainScope.launch {
                Toast.makeText(context, "lettersCursor == null", Toast.LENGTH_LONG).show()
            }
        } else {
            Log.i(TAG, "lettersCursor.getCount(): " + lettersCursor.count)

            var isLast = false
            while (!isLast) {
                lettersCursor.moveToNext()

                // Convert from Room to Gson
                val letterGson = getLetterGson(lettersCursor)
                letterGsons.add(letterGson)

                isLast = lettersCursor.isLast
            }

            lettersCursor.close()
            Log.i(TAG, "lettersCursor.isClosed(): " + lettersCursor.isClosed)
        }

        val soundGsons = mutableListOf<SoundGson>()
        val soundsUri =
            Uri.parse("content://$contentProviderApplicationId.provider.sound_provider/sounds/by-letter-sound-id/$id")
        Log.i(TAG, "soundsUri: $soundsUri")
        val soundsCursor = context.contentResolver.query(soundsUri, null, null, null, null)
        if (soundsCursor == null) {
            Log.e(TAG, "soundsCursor == null")
            mainScope.launch {
                Toast.makeText(context, "soundsCursor == null", Toast.LENGTH_LONG).show()
            }
        } else {
            Log.i(TAG, "soundsCursor.getCount(): " + soundsCursor.count)

            var isLast = false
            while (!isLast) {
                soundsCursor.moveToNext()

                // Convert from Room to Gson
                val soundGson = getSoundGson(soundsCursor)
                soundGsons.add(soundGson)

                isLast = soundsCursor.isLast
            }

            soundsCursor.close()
            Log.i(TAG, "soundsCursor.isClosed(): " + soundsCursor.isClosed)
        }

        return LetterSoundGson().apply {
            this.id = id
            this.revisionNumber = revisionNumber
            this.usageCount = usageCount
            this.letters = letterGsons
            this.sounds = soundGsons
        }
    }
}
