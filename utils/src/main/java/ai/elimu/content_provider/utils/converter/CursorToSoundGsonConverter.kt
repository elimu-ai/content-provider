package ai.elimu.content_provider.utils.converter;

import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;

import ai.elimu.model.v2.gson.content.SoundGson;

public class CursorToSoundGsonConverter {

    public static SoundGson getSoundGson(Cursor cursor) {
        Log.i(CursorToSoundGsonConverter.class.getName(), "getSoundGson");

        Log.i(CursorToSoundGsonConverter.class.getName(), "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnIndexId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnIndexId);
        Log.i(CursorToSoundGsonConverter.class.getName(), "id: " + id);

        int columnIndexRevisionNumber = cursor.getColumnIndex("revisionNumber");
        Integer revisionNumber = cursor.getInt(columnIndexRevisionNumber);
        Log.i(CursorToSoundGsonConverter.class.getName(), "revisionNumber: " + revisionNumber);

        int columnIndexValueIpa = cursor.getColumnIndex("valueIpa");
        String valueIpa = cursor.getString(columnIndexValueIpa);
        Log.i(CursorToSoundGsonConverter.class.getName(), "valueIpa: \"" + valueIpa + "\"");

        SoundGson soundGson = new SoundGson();
        soundGson.setId(id);
        soundGson.setRevisionNumber(revisionNumber);
        soundGson.setValueIpa(valueIpa);

        return soundGson;
    }
}
