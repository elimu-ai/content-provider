package ai.elimu.content_provider.utils.converter;

import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;
import java.util.Calendar;

import ai.elimu.model.enums.analytics.LearningEventType;
import ai.elimu.model.v2.gson.analytics.WordLearningEventGson;

public class CursorToWordLearningEventGsonConverter {

    public static WordLearningEventGson getWordLearningEventGson(Cursor cursor) {
        Log.i(CursorToWordLearningEventGsonConverter.class.getName(), "getWordLearningEventGson");

        Log.i(CursorToWordLearningEventGsonConverter.class.getName(), "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnId);
        Log.i(CursorToWordLearningEventGsonConverter.class.getName(), "id: " + id);

        int columnAndroidId = cursor.getColumnIndex("androidId");
        String androidId = cursor.getString(columnAndroidId);
        Log.i(CursorToWordLearningEventGsonConverter.class.getName(), "androidId: \"" + androidId + "\"");

        int columnPackageName = cursor.getColumnIndex("packageName");
        String packageName = cursor.getString(columnPackageName);
        Log.i(CursorToWordLearningEventGsonConverter.class.getName(), "packageName: \"" + packageName + "\"");

        int columnTime = cursor.getColumnIndex("time");
        Long timeAsLong = cursor.getLong(columnTime);
        Log.i(CursorToWordLearningEventGsonConverter.class.getName(), "timeAsLong: " + timeAsLong);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeAsLong);
        Log.i(CursorToWordLearningEventGsonConverter.class.getName(), "time.getTime(): " + time.getTime());

        int columnWordId = cursor.getColumnIndex("wordId");
        Long wordId = cursor.getLong(columnWordId);
        Log.i(CursorToWordLearningEventGsonConverter.class.getName(), "wordId: " + wordId);

        int columnWordText = cursor.getColumnIndex("wordText");
        String wordText = cursor.getString(columnWordText);
        Log.i(CursorToWordLearningEventGsonConverter.class.getName(), "wordText: \"" + wordText + "\"");

        int columnLearningEventType = cursor.getColumnIndex("learningEventType");
        String learningEventTypeAsString = cursor.getString(columnLearningEventType);
        LearningEventType learningEventType = LearningEventType.valueOf(learningEventTypeAsString);
        Log.i(CursorToWordLearningEventGsonConverter.class.getName(), "learningEventType: " + learningEventType);

        WordLearningEventGson wordLearningEventGson = new WordLearningEventGson();
        wordLearningEventGson.setId(id);
        wordLearningEventGson.setAndroidId(androidId);
        wordLearningEventGson.setPackageName(packageName);
        wordLearningEventGson.setTime(time);
        wordLearningEventGson.setWordId(wordId);
        wordLearningEventGson.setWordText(wordText);
        wordLearningEventGson.setLearningEventType(learningEventType);

        return wordLearningEventGson;
    }
}
