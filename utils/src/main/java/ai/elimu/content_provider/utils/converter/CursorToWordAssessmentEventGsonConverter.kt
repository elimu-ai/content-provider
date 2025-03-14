package ai.elimu.content_provider.utils.converter

import ai.elimu.model.v2.gson.analytics.WordAssessmentEventGson
import android.database.Cursor
import android.util.Log
import java.util.Calendar

object CursorToWordAssessmentEventGsonConverter {
    
    private const val TAG = "CursorToWordAssessmentEventGsonConverter"
    
    fun getWordAssessmentEventGson(cursor: Cursor): WordAssessmentEventGson {
        Log.i(TAG, "getWordAssessmentEventGson")

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

        val columnMasteryScore = cursor.getColumnIndex("masteryScore")
        val masteryScore = cursor.getFloat(columnMasteryScore)
        Log.i(TAG, "masteryScore: $masteryScore")

        val columnTimeSpentMs = cursor.getColumnIndex("timeSpentMs")
        val timeSpentMs = cursor.getLong(columnTimeSpentMs)
        Log.i(TAG, "timeSpentMs: $masteryScore")

        val wordAssessmentEventGson = WordAssessmentEventGson()
        wordAssessmentEventGson.id = id
        wordAssessmentEventGson.androidId = androidId
        wordAssessmentEventGson.packageName = packageName
        wordAssessmentEventGson.time = time
        wordAssessmentEventGson.wordId = wordId
        wordAssessmentEventGson.wordText = wordText
        wordAssessmentEventGson.masteryScore = masteryScore
        wordAssessmentEventGson.timeSpentMs = timeSpentMs

        return wordAssessmentEventGson
    }
}
