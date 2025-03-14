package ai.elimu.content_provider.utils.converter

import ai.elimu.model.v2.gson.content.StoryBookParagraphGson
import ai.elimu.model.v2.gson.content.WordGson
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import android.widget.Toast
import java.util.Locale

object CursorToStoryBookParagraphGsonConverter {
    fun getStoryBookParagraphGson(
        cursor: Cursor,
        context: Context,
        contentProviderApplicationId: String
    ): StoryBookParagraphGson {
        Log.i(CursorToStoryBookParagraphGsonConverter::class.java.name, "getStoryBookParagraphGson")

        Log.i(
            CursorToStoryBookParagraphGsonConverter::class.java.name,
            "Arrays.toString(cursor.getColumnNames()): " + cursor.columnNames.contentToString()
        )

        val columnId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnId)
        Log.i(CursorToStoryBookParagraphGsonConverter::class.java.name, "id: $id")

        val columnSortOrder = cursor.getColumnIndex("sortOrder")
        val sortOrder = cursor.getInt(columnSortOrder)
        Log.i(
            CursorToStoryBookParagraphGsonConverter::class.java.name,
            "sortOrder: $sortOrder"
        )

        val columnOriginalText = cursor.getColumnIndex("originalText")
        val originalText = cursor.getString(columnOriginalText)
        Log.i(
            CursorToStoryBookParagraphGsonConverter::class.java.name,
            "originalText: $originalText"
        )

        var wordGsons: MutableList<WordGson>? = null
        val wordsUri =
            Uri.parse("content://$contentProviderApplicationId.provider.word_provider/words/by-paragraph-id/$id")
        Log.i(CursorToImageGsonConverter::class.java.name, "wordsUri: $wordsUri")
        val wordsCursor = context.contentResolver.query(wordsUri, null, null, null, null)
        if (wordsCursor == null) {
            Log.e(CursorToImageGsonConverter::class.java.name, "wordsCursor == null")
            Toast.makeText(context, "wordsCursor == null", Toast.LENGTH_LONG).show()
        } else {
            Log.i(
                CursorToImageGsonConverter::class.java.name,
                "wordsCursor.getCount(): " + wordsCursor.count
            )
            if (wordsCursor.count == 0) {
                Log.e(CursorToImageGsonConverter::class.java.name, "wordsCursor.getCount() == 0")
            } else {
                Log.i(
                    CursorToImageGsonConverter::class.java.name,
                    "wordsCursor.getCount(): " + wordsCursor.count
                )

                wordGsons = ArrayList()

                var isLast = false
                while (!isLast) {
                    wordsCursor.moveToNext()

                    // Convert from Room to Gson
                    val wordGson = CursorToWordGsonConverter.getWordGson(wordsCursor)
                    wordGsons.add(wordGson)

                    isLast = wordsCursor.isLast
                }

                wordsCursor.close()
                Log.i(
                    CursorToImageGsonConverter::class.java.name,
                    "wordsCursor.isClosed(): " + wordsCursor.isClosed
                )
            }
        }

        var wordGsonsWithNullObjects: MutableList<WordGson?>? = null
        if (wordGsons != null) {
            // Look for a Word match in the original text, and add null if none was found
            wordGsonsWithNullObjects = ArrayList()
            val wordsInOriginalText =
                originalText.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            Log.i(
                CursorToImageGsonConverter::class.java.name,
                "wordsInOriginalText.length: " + wordsInOriginalText.size
            )
            Log.i(
                CursorToImageGsonConverter::class.java.name,
                "Arrays.toString(wordsInOriginalText): " + wordsInOriginalText.contentToString()
            )
            for (wordInOriginalText in wordsInOriginalText) {
                var wordInOriginalText = wordInOriginalText
                Log.i(
                    CursorToImageGsonConverter::class.java.name,
                    "wordInOriginalText (before cleaning): \"$wordInOriginalText\""
                )
                wordInOriginalText = wordInOriginalText
                    .replace(",", "")
                    .replace("\"", "")
                    .replace("“", "")
                    .replace("”", "")
                    .replace(".", "")
                    .replace("!", "")
                    .replace("?", "")
                    .replace(":", "")
                    .replace("(", "")
                    .replace(")", "")
                wordInOriginalText = wordInOriginalText.trim { it <= ' ' }
                wordInOriginalText = wordInOriginalText.lowercase(Locale.getDefault())
                Log.i(
                    CursorToImageGsonConverter::class.java.name,
                    "wordInOriginalText (after cleaning): \"$wordInOriginalText\""
                )

                var wordGsonMatch: WordGson? = null
                for (wordGson in wordGsons) {
                    Log.i(
                        CursorToImageGsonConverter::class.java.name,
                        "wordGson.getText(): \"" + wordGson.text + "\""
                    )
                    if (wordGson.text == wordInOriginalText) {
                        wordGsonMatch = wordGson
                        break
                    }
                }
                wordGsonsWithNullObjects.add(wordGsonMatch)
            }
            Log.i(
                CursorToImageGsonConverter::class.java.name,
                "wordGsonsWithNullObjects.size(): " + wordGsonsWithNullObjects.size
            )
        }

        val storyBookParagraph = StoryBookParagraphGson()
        storyBookParagraph.id = id
        storyBookParagraph.sortOrder = sortOrder
        storyBookParagraph.originalText = originalText
        storyBookParagraph.words = wordGsonsWithNullObjects

        return storyBookParagraph
    }
}
