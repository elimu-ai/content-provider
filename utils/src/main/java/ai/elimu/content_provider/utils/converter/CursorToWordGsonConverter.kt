package ai.elimu.content_provider.utils.converter

import ai.elimu.model.v2.enums.content.WordType
import ai.elimu.model.v2.gson.content.WordGson
import android.database.Cursor
import android.text.TextUtils
import android.util.Log

object CursorToWordGsonConverter {
    
    private const val TAG = "CursorToWordGsonConverter"
    
    fun getWordGson(cursor: Cursor): WordGson {
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
        word.wordType = wordType

        return word
    }
}
