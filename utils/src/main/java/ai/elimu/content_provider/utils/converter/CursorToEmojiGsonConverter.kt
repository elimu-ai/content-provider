package ai.elimu.content_provider.utils.converter

import ai.elimu.model.v2.gson.content.EmojiGson
import android.database.Cursor
import android.util.Log

object CursorToEmojiGsonConverter {
    fun getEmojiGson(cursor: Cursor): EmojiGson {
        Log.i(CursorToEmojiGsonConverter::class.java.name, "getEmojiGson")

        Log.i(
            CursorToEmojiGsonConverter::class.java.name,
            "Arrays.toString(cursor.getColumnNames()): " + cursor.columnNames.contentToString()
        )

        val columnId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnId)
        Log.i(CursorToEmojiGsonConverter::class.java.name, "id: $id")

        val columnRevisionNumber = cursor.getColumnIndex("revisionNumber")
        val revisionNumber = cursor.getInt(columnRevisionNumber)
        Log.i(
            CursorToEmojiGsonConverter::class.java.name,
            "revisionNumber: $revisionNumber"
        )

        val columnUsageCount = cursor.getColumnIndex("usageCount")
        val usageCount = cursor.getInt(columnUsageCount)
        Log.i(
            CursorToEmojiGsonConverter::class.java.name,
            "usageCount: $usageCount"
        )

        val columnGlyph = cursor.getColumnIndex("glyph")
        val glyph = cursor.getString(columnGlyph)
        Log.i(CursorToEmojiGsonConverter::class.java.name, "glyph: \"$glyph\"")

        val columnUnicodeVersion = cursor.getColumnIndex("unicodeVersion")
        val unicodeVersion = cursor.getDouble(columnUnicodeVersion)
        Log.i(
            CursorToEmojiGsonConverter::class.java.name,
            "unicodeVersion: $unicodeVersion"
        )

        val columnUnicodeEmojiVersion = cursor.getColumnIndex("unicodeEmojiVersion")
        val unicodeEmojiVersion = cursor.getDouble(columnUnicodeEmojiVersion)
        Log.i(
            CursorToEmojiGsonConverter::class.java.name,
            "unicodeEmojiVersion: $unicodeEmojiVersion"
        )

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
