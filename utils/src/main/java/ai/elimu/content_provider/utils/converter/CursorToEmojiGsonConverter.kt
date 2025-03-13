package ai.elimu.content_provider.utils.converter;

import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;

import ai.elimu.model.v2.gson.content.EmojiGson;

public class CursorToEmojiGsonConverter {

    public static EmojiGson getEmojiGson(Cursor cursor) {
        Log.i(CursorToEmojiGsonConverter.class.getName(), "getEmojiGson");

        Log.i(CursorToEmojiGsonConverter.class.getName(), "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnId);
        Log.i(CursorToEmojiGsonConverter.class.getName(), "id: " + id);

        int columnRevisionNumber = cursor.getColumnIndex("revisionNumber");
        Integer revisionNumber = cursor.getInt(columnRevisionNumber);
        Log.i(CursorToEmojiGsonConverter.class.getName(), "revisionNumber: " + revisionNumber);

        int columnUsageCount = cursor.getColumnIndex("usageCount");
        Integer usageCount = cursor.getInt(columnUsageCount);
        Log.i(CursorToEmojiGsonConverter.class.getName(), "usageCount: " + usageCount);

        int columnGlyph = cursor.getColumnIndex("glyph");
        String glyph = cursor.getString(columnGlyph);
        Log.i(CursorToEmojiGsonConverter.class.getName(), "glyph: \"" + glyph + "\"");

        int columnUnicodeVersion = cursor.getColumnIndex("unicodeVersion");
        Double unicodeVersion = cursor.getDouble(columnUnicodeVersion);
        Log.i(CursorToEmojiGsonConverter.class.getName(), "unicodeVersion: " + unicodeVersion);

        int columnUnicodeEmojiVersion = cursor.getColumnIndex("unicodeEmojiVersion");
        Double unicodeEmojiVersion = cursor.getDouble(columnUnicodeEmojiVersion);
        Log.i(CursorToEmojiGsonConverter.class.getName(), "unicodeEmojiVersion: " + unicodeEmojiVersion);

        EmojiGson emoji = new EmojiGson();
        emoji.setId(id);
        emoji.setRevisionNumber(revisionNumber);
        emoji.setUsageCount(usageCount);
        emoji.setGlyph(glyph);
        emoji.setUnicodeVersion(unicodeVersion);
        emoji.setUnicodeEmojiVersion(unicodeEmojiVersion);

        return emoji;
    }
}
