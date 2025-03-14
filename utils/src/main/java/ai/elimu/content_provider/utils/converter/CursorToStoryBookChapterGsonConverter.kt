package ai.elimu.content_provider.utils.converter

import ai.elimu.content_provider.utils.converter.CursorToImageGsonConverter.getImageGson
import ai.elimu.model.v2.gson.content.ImageGson
import ai.elimu.model.v2.gson.content.StoryBookChapterGson
import ai.elimu.model.v2.gson.content.StoryBookParagraphGson
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import android.widget.Toast

object CursorToStoryBookChapterGsonConverter {
    fun getStoryBookChapterGson(
        cursor: Cursor,
        context: Context,
        contentProviderApplicationId: String
    ): StoryBookChapterGson {
        Log.i(CursorToStoryBookChapterGsonConverter::class.java.name, "getStoryBookChapterGson")

        Log.i(
            CursorToStoryBookChapterGsonConverter::class.java.name,
            "Arrays.toString(cursor.getColumnNames()): " + cursor.columnNames.contentToString()
        )

        val columnId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnId)
        Log.i(CursorToStoryBookChapterGsonConverter::class.java.name, "id: $id")

        val columnSortOrder = cursor.getColumnIndex("sortOrder")
        val sortOrder = cursor.getInt(columnSortOrder)
        Log.i(
            CursorToStoryBookChapterGsonConverter::class.java.name,
            "sortOrder: $sortOrder"
        )

        var imageGson: ImageGson? = null
        val columnImageId = cursor.getColumnIndex("imageId")
        val imageId = cursor.getLong(columnImageId)
        Log.i(CursorToImageGsonConverter::class.java.name, "imageId: $imageId")
        if (imageId != null) {
            val imageUri =
                Uri.parse("content://$contentProviderApplicationId.provider.image_provider/images/$imageId")
            Log.i(
                CursorToImageGsonConverter::class.java.name,
                "imageUri: $imageUri"
            )
            val imageCursor = context.contentResolver.query(imageUri, null, null, null, null)
            if (imageCursor == null) {
                Log.e(CursorToImageGsonConverter::class.java.name, "imageCursor == null")
                Toast.makeText(context, "imageCursor == null", Toast.LENGTH_LONG).show()
            } else {
                Log.i(
                    CursorToImageGsonConverter::class.java.name,
                    "imageCursor.getCount(): " + imageCursor.count
                )
                if (imageCursor.count == 0) {
                    Log.e(
                        CursorToImageGsonConverter::class.java.name,
                        "imageCursor.getCount() == 0"
                    )
                } else {
                    Log.i(
                        CursorToImageGsonConverter::class.java.name,
                        "imageCursor.getCount(): " + imageCursor.count
                    )

                    imageCursor.moveToFirst()

                    // Convert from Room to Gson
                    imageGson = getImageGson(imageCursor)

                    imageCursor.close()
                    Log.i(
                        CursorToImageGsonConverter::class.java.name,
                        "imageCursor.isClosed(): " + imageCursor.isClosed
                    )
                }
            }
        }

        var paragraphGsons: MutableList<StoryBookParagraphGson?>? = null
        val paragraphsUri =
            Uri.parse("content://$contentProviderApplicationId.provider.storybook_provider/storybooks/0/chapters/$id/paragraphs")
        Log.i(
            CursorToImageGsonConverter::class.java.name,
            "paragraphsUri: $paragraphsUri"
        )
        val paragraphsCursor = context.contentResolver.query(paragraphsUri, null, null, null, null)
        if (paragraphsCursor == null) {
            Log.e(CursorToImageGsonConverter::class.java.name, "paragraphsCursor == null")
            Toast.makeText(context, "paragraphsCursor == null", Toast.LENGTH_LONG).show()
        } else {
            Log.i(
                CursorToImageGsonConverter::class.java.name,
                "paragraphsCursor.getCount(): " + paragraphsCursor.count
            )
            if (paragraphsCursor.count == 0) {
                Log.e(
                    CursorToImageGsonConverter::class.java.name,
                    "paragraphsCursor.getCount() == 0"
                )
            } else {
                Log.i(
                    CursorToImageGsonConverter::class.java.name,
                    "paragraphsCursor.getCount(): " + paragraphsCursor.count
                )

                paragraphGsons = ArrayList()

                var isLast = false
                while (!isLast) {
                    paragraphsCursor.moveToNext()

                    // Convert from Room to Gson
                    val storyBookParagraphGson =
                        CursorToStoryBookParagraphGsonConverter.getStoryBookParagraphGson(
                            paragraphsCursor,
                            context,
                            contentProviderApplicationId
                        )
                    paragraphGsons.add(storyBookParagraphGson)

                    isLast = paragraphsCursor.isLast
                }

                paragraphsCursor.close()
                Log.i(
                    CursorToImageGsonConverter::class.java.name,
                    "paragraphsCursor.isClosed(): " + paragraphsCursor.isClosed
                )
            }
        }

        val storyBookChapter = StoryBookChapterGson()
        storyBookChapter.id = id
        storyBookChapter.sortOrder = sortOrder
        storyBookChapter.image = imageGson
        storyBookChapter.storyBookParagraphs = paragraphGsons

        return storyBookChapter
    }
}
