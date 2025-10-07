package ai.elimu.content_provider.utils.converter

import ai.elimu.model.v2.enums.content.WordType
import ai.elimu.model.v2.gson.content.LetterSoundGson
import ai.elimu.model.v2.gson.content.WordGson
import android.content.Context
import android.database.Cursor
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri

object CursorToWordGsonConverter {
    
    private const val TAG = "CursorToWordGsonConverter"
    
    fun getWordGson(cursor: Cursor, context: Context, contentProviderApplicationId: String): WordGson {
        Log.i(TAG, "getWordGson")

        Log.i(TAG,
            "Arrays.toString(cursor.getColumnNames()): " + cursor.columnNames.contentToString())

        val columnId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnId)
        Log.i(TAG, "id: $id")

        val columnRevisionNumber = cursor.getColumnIndex("revisionNumber")
        val revisionNumber = cursor.getInt(columnRevisionNumber)
        Log.i(TAG, "revisionNumber: $revisionNumber")

        val columnUsageCount = cursor.getColumnIndex("usageCount")
        val usageCount = cursor.getInt(columnUsageCount)
        Log.i(TAG, "usageCount: $usageCount")

        val columnText = cursor.getColumnIndex("text")
        val text = cursor.getString(columnText)
        Log.i(TAG, "text: \"$text\"")

        val letterSoundGsons = mutableListOf<LetterSoundGson>()
        val letterSoundsUri = "content://${contentProviderApplicationId}.provider.letter_sound_provider/letter_sounds/by-word-id/${id}".toUri()
        Log.i(this::class.simpleName, "letterSoundsUri: ${letterSoundsUri}")
        val letterSoundsCursor = context.contentResolver.query(letterSoundsUri, null, null, null, null)
        if (letterSoundsCursor == null) {
            Log.e(this::class.simpleName, "letterSoundsCursor == null")
            Toast.makeText(context, "letterSoundsCursor == null", Toast.LENGTH_LONG).show()
        } else {
            Log.i(this::class.simpleName, "letterSoundsCursor.count: ${letterSoundsCursor.count}")

            var isLast = false
            while (!isLast) {
                letterSoundsCursor.moveToNext()

                // Convert from Room to Gson
                val letterSoundGson = CursorToLetterSoundGsonConverter.getLetterSoundGson(letterSoundsCursor, context, contentProviderApplicationId)
                letterSoundGsons.add(letterSoundGson)

                isLast = letterSoundsCursor.isLast
            }
        }

        val columnWordType = cursor.getColumnIndex("wordType")
        val wordTypeAsString = cursor.getString(columnWordType)
        var wordType: WordType? = null
        if (!TextUtils.isEmpty(wordTypeAsString)) {
            wordType = WordType.valueOf(wordTypeAsString)
        }
        Log.i(TAG, "wordType: $wordType")

        val word = WordGson()
        word.id = id
        word.revisionNumber = revisionNumber
        word.usageCount = usageCount
        word.text = text
        word.letterSounds = letterSoundGsons
        word.wordType = wordType

        return word
    }
}
