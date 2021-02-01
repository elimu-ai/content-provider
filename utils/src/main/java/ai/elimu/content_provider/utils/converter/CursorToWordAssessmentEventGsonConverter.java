package ai.elimu.content_provider.utils.converter;

import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;
import java.util.Calendar;

import ai.elimu.model.v2.gson.analytics.WordAssessmentEventGson;

public class CursorToWordAssessmentEventGsonConverter {

    public static WordAssessmentEventGson getWordAssessmentEventGson(Cursor cursor) {
        Log.i(CursorToWordAssessmentEventGsonConverter.class.getName(), "getWordAssessmentEventGson");

        Log.i(CursorToWordAssessmentEventGsonConverter.class.getName(), "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnId);
        Log.i(CursorToWordAssessmentEventGsonConverter.class.getName(), "id: " + id);

        int columnAndroidId = cursor.getColumnIndex("androidId");
        String androidId = cursor.getString(columnAndroidId);
        Log.i(CursorToWordAssessmentEventGsonConverter.class.getName(), "androidId: \"" + androidId + "\"");

        int columnPackageName = cursor.getColumnIndex("packageName");
        String packageName = cursor.getString(columnPackageName);
        Log.i(CursorToWordAssessmentEventGsonConverter.class.getName(), "packageName: \"" + packageName + "\"");

        int columnTime = cursor.getColumnIndex("time");
        Long timeAsLong = cursor.getLong(columnTime);
        Log.i(CursorToWordAssessmentEventGsonConverter.class.getName(), "timeAsLong: " + timeAsLong);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeAsLong);
        Log.i(CursorToWordAssessmentEventGsonConverter.class.getName(), "time.getTime(): " + time.getTime());

        int columnWordId = cursor.getColumnIndex("wordId");
        Long wordId = cursor.getLong(columnWordId);
        Log.i(CursorToWordAssessmentEventGsonConverter.class.getName(), "wordId: " + wordId);

        int columnWordText = cursor.getColumnIndex("wordText");
        String wordText = cursor.getString(columnWordText);
        Log.i(CursorToWordAssessmentEventGsonConverter.class.getName(), "wordText: \"" + wordText + "\"");

        int columnMasteryScore = cursor.getColumnIndex("masteryScore");
        Float masteryScore = cursor.getFloat(columnMasteryScore);
        Log.i(CursorToWordAssessmentEventGsonConverter.class.getName(), "masteryScore: " + masteryScore);

        int columnTimeSpentMs = cursor.getColumnIndex("timeSpentMs");
        Long timeSpentMs = cursor.getLong(columnTimeSpentMs);
        Log.i(CursorToWordAssessmentEventGsonConverter.class.getName(), "timeSpentMs: " + masteryScore);

        WordAssessmentEventGson wordAssessmentEventGson = new WordAssessmentEventGson();
        wordAssessmentEventGson.setId(id);
        wordAssessmentEventGson.setAndroidId(androidId);
        wordAssessmentEventGson.setPackageName(packageName);
        wordAssessmentEventGson.setTime(time);
        wordAssessmentEventGson.setWordId(wordId);
        wordAssessmentEventGson.setWordText(wordText);
        wordAssessmentEventGson.setMasteryScore(masteryScore);
        wordAssessmentEventGson.setTimeSpentMs(timeSpentMs);

        return wordAssessmentEventGson;
    }
}
