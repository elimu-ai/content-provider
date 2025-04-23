package ai.elimu.content_provider.utils.converter

import ai.elimu.model.v2.gson.content.EmojiGson
import android.database.Cursor
import android.util.Log

object CursorToEmojiGsonConverter {
    
    private const val TAG = "CursorToEmojiGsonConverter"
    
    fun getEmojiGson(cursor: Cursor): EmojiGson {
        Log.i(TAG, "getEmojiGson")

        Log.i(TAG, "Arrays.toString(cursor.getColumnNames()): " + cursor.columnNames.contentToString())

        val columnId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnId)
        Log.i(TAG, "id: $id")

        val columnRevisionNumber = cursor.getColumnIndex("revisionNumber")
        val revisionNumber = cursor.getInt(columnRevisionNumber)
        Log.i(TAG, "revisionNumber: $revisionNumber")

        val columnUsageCount = cursor.getColumnIndex("usageCount")
        val usageCount = cursor.getInt(columnUsageCount)
        Log.i(TAG, "usageCount: $usageCount")

        val columnGlyph = cursor.getColumnIndex("glyph")
        val glyph = cursor.getString(columnGlyph)
        Log.i(TAG, "glyph: \"$glyph\"")

        val columnUnicodeVersion = cursor.getColumnIndex("unicodeVersion")
        val unicodeVersion = cursor.getDouble(columnUnicodeVersion)
        Log.i(TAG, "unicodeVersion: $unicodeVersion")

        val columnUnicodeEmojiVersion = cursor.getColumnIndex("unicodeEmojiVersion")
        val unicodeEmojiVersion = cursor.getDouble(columnUnicodeEmojiVersion)
        Log.i(TAG, "unicodeEmojiVersion: $unicodeEmojiVersion")

        val emoji = EmojiGson()
        emoji.id = id
        emoji.revisionNumber = revisionNumber
        emoji.usageCount = usageCount
        emoji.glyph = glyph
        emoji.unicodeVersion = unicodeVersion
        emoji.unicodeEmojiVersion = unicodeEmojiVersion

        return emoji
    }
}
