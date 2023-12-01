package ai.elimu.content_provider.utils.converter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.elimu.model.v2.gson.content.LetterGson;
import ai.elimu.model.v2.gson.content.LetterSoundGson;

public class CursorToLetterSoundGsonConverter {

    public static LetterSoundGson getLetterSoundGson(Cursor cursor, Context context, String contentProviderApplicationId) {
        Log.i(CursorToLetterSoundGsonConverter.class.getName(), "getLetterSoundGson");

        Log.i(CursorToLetterSoundGsonConverter.class.getName(), "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnIndexId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnIndexId);
        Log.i(CursorToLetterSoundGsonConverter.class.getName(), "id: " + id);

        int columnIndexRevisionNumber = cursor.getColumnIndex("revisionNumber");
        Integer revisionNumber = cursor.getInt(columnIndexRevisionNumber);
        Log.i(CursorToLetterSoundGsonConverter.class.getName(), "revisionNumber: " + revisionNumber);

        int columnIndexUsageCount = cursor.getColumnIndex("usageCount");
        Integer usageCount = cursor.getInt(columnIndexUsageCount);
        Log.i(CursorToLetterSoundGsonConverter.class.getName(), "usageCount: " + usageCount);

        List<LetterGson> letterGsons = null;
        Uri lettersUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.letter_provider/letters/by-letter-sound-id/" + id);
        Log.i(CursorToLetterSoundGsonConverter.class.getName(), "lettersUri: " + lettersUri);
        Cursor lettersCursor = context.getContentResolver().query(lettersUri, null, null, null, null);
        if (lettersCursor == null) {
            Log.e(CursorToLetterSoundGsonConverter.class.getName(), "lettersCursor == null");
            Toast.makeText(context, "lettersCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(CursorToLetterSoundGsonConverter.class.getName(), "lettersCursor.getCount(): " + lettersCursor.getCount());

            letterGsons = new ArrayList<>();

            boolean isLast = false;
            while (!isLast) {
                lettersCursor.moveToNext();

                // Convert from Room to Gson
                LetterGson letterGson = CursorToLetterGsonConverter.getLetterGson(lettersCursor);
                letterGsons.add(letterGson);

                isLast = lettersCursor.isLast();
            }

            lettersCursor.close();
            Log.i(CursorToLetterSoundGsonConverter.class.getName(), "lettersCursor.isClosed(): " + lettersCursor.isClosed());
        }

        LetterSoundGson letterSound = new LetterSoundGson();
        letterSound.setId(id);
        letterSound.setRevisionNumber(revisionNumber);
        letterSound.setUsageCount(usageCount);
        letterSound.setLetters(letterGsons);
        // TODO: letterSound.setSounds(sounds);

        return letterSound;
    }
}
