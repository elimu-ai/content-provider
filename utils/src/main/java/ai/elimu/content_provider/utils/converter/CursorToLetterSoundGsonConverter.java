package ai.elimu.content_provider.utils.converter;

import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;

import ai.elimu.model.v2.gson.content.LetterSoundGson;

public class CursorToLetterSoundGsonConverter {

    public static LetterSoundGson getLetterSoundGson(Cursor cursor) {
        Log.i(CursorToLetterSoundGsonConverter.class.getName(), "getLetterSoundGson");

        Log.i(CursorToLetterSoundGsonConverter.class.getName(), "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnIndexId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnIndexId);
        Log.i(CursorToLetterSoundGsonConverter.class.getName(), "id: " + id);

        int columnIndexRevisionNumber = cursor.getColumnIndex("revisionNumber");
        Integer revisionNumber = cursor.getInt(columnIndexRevisionNumber);
        Log.i(CursorToLetterSoundGsonConverter.class.getName(), "revisionNumber: " + revisionNumber);

        int columnIndexText = cursor.getColumnIndex("text");
        String text = cursor.getString(columnIndexText);
        Log.i(CursorToLetterSoundGsonConverter.class.getName(), "text: \"" + text + "\"");

        int columnIndexIsDiacritic = cursor.getColumnIndex("diacritic");
        Boolean isDiacritic = (cursor.getInt(columnIndexIsDiacritic) > 0);
        Log.i(CursorToLetterSoundGsonConverter.class.getName(), "isDiacritic: " + isDiacritic);

        LetterSoundGson letterSound = new LetterSoundGson();
        letterSound.setId(id);
        letterSound.setRevisionNumber(revisionNumber);
        // TODO: letterSound.setLetters(letters);
        // TODO: letterSound.setSounds(sounds);

        return letterSound;
    }
}
