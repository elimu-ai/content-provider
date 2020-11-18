package ai.elimu.content_provider.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.elimu.model.v2.gson.content.LetterGson;

public class ContentProviderHelper {

    /**
     * Fetches {@code LetterGson}s from the elimu.ai Content Provider (see https://github.com/elimu-ai/content-provider)
     */
    public static List<LetterGson> getLetterGsons(Context context, String contentProviderApplicationId) {
        List<LetterGson> letterGsons = new ArrayList<>();

        Uri uri = Uri.parse("content://" + contentProviderApplicationId + ".provider.letter_provider/letters");
        Log.i(ContentProviderHelper.class.getName(), "uri: " + uri);
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        Log.i(ContentProviderHelper.class.getName(), "cursor: " + cursor);
        if (cursor == null) {
            Log.e(ContentProviderHelper.class.getName(), "cursor == null");
            Toast.makeText(context, "cursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderHelper.class.getName(), "cursor.getCount(): " + cursor.getCount());
            if (cursor.getCount() == 0) {
                Log.e(ContentProviderHelper.class.getName(), "cursor.getCount() == 0");
            } else {
                boolean isLast = false;
                while (!isLast) {
                    cursor.moveToNext();

                    // Convert from Cursor to Gson
                    LetterGson letterGson = getLetterGson(cursor);

                    letterGsons.add(letterGson);

                    isLast = cursor.isLast();
                }
                cursor.close();
                Log.i(ContentProviderHelper.class.getName(), "cursor.isClosed(): " + cursor.isClosed());
            }
        }

        return letterGsons;
    }

    private static LetterGson getLetterGson(Cursor cursor) {
        Log.i(ContentProviderHelper.class.getName(), "getLetterGson");

        Log.i(ContentProviderHelper.class.getName(), "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnIndexId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnIndexId);
        Log.i(ContentProviderHelper.class.getName(), "id: " + id);

        int columnIndexRevisionNumber = cursor.getColumnIndex("revisionNumber");
        Integer revisionNumber = cursor.getInt(columnIndexRevisionNumber);
        Log.i(ContentProviderHelper.class.getName(), "revisionNumber: " + revisionNumber);

        int columnIndexText = cursor.getColumnIndex("text");
        String text = cursor.getString(columnIndexText);
        Log.i(ContentProviderHelper.class.getName(), "text: \"" + text + "\"");

        int columnIndexIsDiacritic = cursor.getColumnIndex("diacritic");
        Boolean isDiacritic = (cursor.getInt(columnIndexIsDiacritic) > 0);
        Log.i(ContentProviderHelper.class.getName(), "isDiacritic: " + isDiacritic);

        LetterGson letter = new LetterGson();
        letter.setId(id);
        letter.setRevisionNumber(revisionNumber);
        letter.setText(text);
        letter.setDiacritic(isDiacritic);

        return letter;
    }
}
