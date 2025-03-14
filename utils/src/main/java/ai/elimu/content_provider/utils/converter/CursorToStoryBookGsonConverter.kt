package ai.elimu.content_provider.utils.converter

import ai.elimu.model.v2.enums.ReadingLevel
import ai.elimu.model.v2.gson.content.ImageGson
import ai.elimu.model.v2.gson.content.StoryBookGson
import android.database.Cursor
import android.util.Log

object CursorToStoryBookGsonConverter {

    private const val TAG = "CursorToStoryBookGsonConverter"

    fun getStoryBookGson(cursor: Cursor): StoryBookGson {
        Log.i(TAG, "getStoryBookGson")

        Log.i(TAG, "Arrays.toString(cursor.getColumnNames()): "
                + cursor.columnNames.contentToString())

        val columnId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnId)
        Log.i(TAG, "id: $id")

        val columnTitle = cursor.getColumnIndex("title")
        val title = cursor.getString(columnTitle)
        Log.i(TAG, "title: \"$title\"")

        val columnDescription = cursor.getColumnIndex("description")
        val description = cursor.getString(columnDescription)
        Log.i(TAG, "description: \"$description\"")

        val columnCoverImageId = cursor.getColumnIndex("coverImageId")
        val coverImageId = cursor.getLong(columnCoverImageId)
        Log.i(TAG, "coverImageId: $coverImageId")
        val coverImage = ImageGson()
        coverImage.id = coverImageId

        val columnReadingLevel = cursor.getColumnIndex("readingLevel")
        val readingLevelName = cursor.getString(columnReadingLevel)
        var readingLevel: ReadingLevel? = null
        if (readingLevelName != null) {
            readingLevel = ReadingLevel.valueOf(readingLevelName)
        }
        Log.i(TAG, "readingLevel: $readingLevel")

        val storyBook = StoryBookGson()
        storyBook.id = id
        storyBook.title = title
        storyBook.description = description
        storyBook.coverImage = coverImage
        storyBook.readingLevel = readingLevel

        return storyBook
    }
}
