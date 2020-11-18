package ai.elimu.content_provider.utils.converter;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;

import ai.elimu.model.enums.content.WordType;
import ai.elimu.model.v2.gson.content.WordGson;

public class CursorToWordGsonConverter {

    public static WordGson getWordGson(Cursor cursor) {
        Log.i(CursorToWordGsonConverter.class.getName(), "getWordGson");

        Log.i(CursorToWordGsonConverter.class.getName(), "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnId);
        Log.i(CursorToWordGsonConverter.class.getName(), "id: " + id);

        int columnRevisionNumber = cursor.getColumnIndex("revisionNumber");
        Integer revisionNumber = cursor.getInt(columnRevisionNumber);
        Log.i(CursorToWordGsonConverter.class.getName(), "revisionNumber: " + revisionNumber);

        int columnUsageCount = cursor.getColumnIndex("usageCount");
        Integer usageCount = cursor.getInt(columnUsageCount);
        Log.i(CursorToWordGsonConverter.class.getName(), "usageCount: " + usageCount);

        int columnText = cursor.getColumnIndex("text");
        String text = cursor.getString(columnText);
        Log.i(CursorToWordGsonConverter.class.getName(), "text: \"" + text + "\"");

        int columnWordType = cursor.getColumnIndex("wordType");
        String wordTypeAsString = cursor.getString(columnWordType);
        WordType wordType = null;
        if (!TextUtils.isEmpty(wordTypeAsString)) {
            wordType = WordType.valueOf(wordTypeAsString);
        }
        Log.i(CursorToWordGsonConverter.class.getName(), "wordType: " + wordType);

        WordGson word = new WordGson();
        word.setId(id);
        word.setRevisionNumber(revisionNumber);
        word.setUsageCount(usageCount);
        word.setText(text);
        word.setWordType(wordType);

        return word;
    }
}
