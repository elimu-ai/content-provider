package ai.elimu.content_provider.utils.converter

import ai.elimu.model.v2.gson.analytics.WordLearningEventGson
import android.database.Cursor
import android.util.Log
import java.util.Calendar

object CursorToWordLearningEventGsonConverter {
    
    private const val TAG = "CursorToWordLearningEventGsonConverter"
    
    fun getWordLearningEventGson(cursor: Cursor): WordLearningEventGson {
        Log.i(TAG, "getWordLearningEventGson")

        Log.i(TAG,
            "Arrays.toString(cursor.getColumnNames()): " + cursor.columnNames.contentToString())

        val columnId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnId)
        Log.i(TAG, "id: $id")

        val columnAndroidId = cursor.getColumnIndex("androidId")
        val androidId = cursor.getString(columnAndroidId)
        Log.i(TAG, "androidId: \"$androidId\"")

        val columnPackageName = cursor.getColumnIndex("packageName")
        val packageName = cursor.getString(columnPackageName)
        Log.i(TAG, "packageName: \"$packageName\"")

        val columnTime = cursor.getColumnIndex("time")
        val timeAsLong = cursor.getLong(columnTime)
        Log.i(TAG, "timeAsLong: $timeAsLong")
        val time = Calendar.getInstance()
        time.timeInMillis = timeAsLong
        Log.i(TAG, "time.getTime(): " + time.time)

        val columnWordId = cursor.getColumnIndex("wordId")
        val wordId = cursor.getLong(columnWordId)
        Log.i(TAG, "wordId: $wordId")

        val columnWordText = cursor.getColumnIndex("wordText")
        val wordText = cursor.getString(columnWordText)
        Log.i(TAG, "wordText: \"$wordText\"")

        val wordLearningEventGson = WordLearningEventGson()
        wordLearningEventGson.id = id
        wordLearningEventGson.androidId = androidId
        wordLearningEventGson.packageName = packageName
        wordLearningEventGson.timestamp = time
        wordLearningEventGson.wordId = wordId
        wordLearningEventGson.wordText = wordText

        return wordLearningEventGson
    }
}
