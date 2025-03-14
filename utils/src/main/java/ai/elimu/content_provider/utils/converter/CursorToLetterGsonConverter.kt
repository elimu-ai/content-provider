package ai.elimu.content_provider.utils.converter

import ai.elimu.model.v2.gson.content.LetterGson
import android.database.Cursor
import android.util.Log

object CursorToLetterGsonConverter {
    @JvmStatic
    fun getLetterGson(cursor: Cursor): LetterGson {
        Log.i(CursorToLetterGsonConverter::class.java.name, "getLetterGson")

        Log.i(
            CursorToLetterGsonConverter::class.java.name,
            "Arrays.toString(cursor.getColumnNames()): " + cursor.columnNames.contentToString()
        )

        val columnIndexId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnIndexId)
        Log.i(CursorToLetterGsonConverter::class.java.name, "id: $id")

        val columnIndexRevisionNumber = cursor.getColumnIndex("revisionNumber")
        val revisionNumber = cursor.getInt(columnIndexRevisionNumber)
        Log.i(
            CursorToLetterGsonConverter::class.java.name,
            "revisionNumber: $revisionNumber"
        )

        val columnIndexText = cursor.getColumnIndex("text")
        val text = cursor.getString(columnIndexText)
        Log.i(CursorToLetterGsonConverter::class.java.name, "text: \"$text\"")

        val columnIndexIsDiacritic = cursor.getColumnIndex("diacritic")
        val isDiacritic = (cursor.getInt(columnIndexIsDiacritic) > 0)
        Log.i(
            CursorToLetterGsonConverter::class.java.name,
            "isDiacritic: $isDiacritic"
        )

        val letter = LetterGson()
        letter.id = id
        letter.revisionNumber = revisionNumber
        letter.text = text
        letter.diacritic = isDiacritic

        return letter
    }
}
