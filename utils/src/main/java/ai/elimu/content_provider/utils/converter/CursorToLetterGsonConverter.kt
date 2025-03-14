package ai.elimu.content_provider.utils.converter;

import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;

import ai.elimu.model.v2.gson.content.LetterGson;

public class CursorToLetterGsonConverter {

    public static LetterGson getLetterGson(Cursor cursor) {
        Log.i(CursorToLetterGsonConverter.class.getName(), "getLetterGson");

        Log.i(CursorToLetterGsonConverter.class.getName(), "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnIndexId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnIndexId);
        Log.i(CursorToLetterGsonConverter.class.getName(), "id: " + id);

        int columnIndexRevisionNumber = cursor.getColumnIndex("revisionNumber");
        Integer revisionNumber = cursor.getInt(columnIndexRevisionNumber);
        Log.i(CursorToLetterGsonConverter.class.getName(), "revisionNumber: " + revisionNumber);

        int columnIndexText = cursor.getColumnIndex("text");
        String text = cursor.getString(columnIndexText);
        Log.i(CursorToLetterGsonConverter.class.getName(), "text: \"" + text + "\"");

        int columnIndexIsDiacritic = cursor.getColumnIndex("diacritic");
        Boolean isDiacritic = (cursor.getInt(columnIndexIsDiacritic) > 0);
        Log.i(CursorToLetterGsonConverter.class.getName(), "isDiacritic: " + isDiacritic);

        LetterGson letter = new LetterGson();
        letter.setId(id);
        letter.setRevisionNumber(revisionNumber);
        letter.setText(text);
        letter.setDiacritic(isDiacritic);

        return letter;
    }
}
