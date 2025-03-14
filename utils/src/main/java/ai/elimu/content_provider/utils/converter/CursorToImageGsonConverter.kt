package ai.elimu.content_provider.utils.converter

import ai.elimu.model.v2.enums.content.ImageFormat
import ai.elimu.model.v2.gson.content.ImageGson
import android.database.Cursor
import android.util.Log

object CursorToImageGsonConverter {
    @JvmStatic
    fun getImageGson(cursor: Cursor): ImageGson {
        Log.i(CursorToImageGsonConverter::class.java.name, "getImageGson")

        Log.i(
            CursorToImageGsonConverter::class.java.name,
            "Arrays.toString(cursor.getColumnNames()): " + cursor.columnNames.contentToString()
        )

        val columnId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnId)
        Log.i(CursorToImageGsonConverter::class.java.name, "id: $id")

        val columnRevisionNumber = cursor.getColumnIndex("revisionNumber")
        val revisionNumber = cursor.getInt(columnRevisionNumber)
        Log.i(
            CursorToImageGsonConverter::class.java.name,
            "revisionNumber: $revisionNumber"
        )

        val columnTitle = cursor.getColumnIndex("title")
        val title = cursor.getString(columnTitle)
        Log.i(CursorToImageGsonConverter::class.java.name, "title: \"$title\"")

        val columnImageFormat = cursor.getColumnIndex("imageFormat")
        val imageFormatAsString = cursor.getString(columnImageFormat)
        Log.i(
            CursorToImageGsonConverter::class.java.name,
            "imageFormatAsString: $imageFormatAsString"
        )
        val imageFormat = ImageFormat.valueOf(imageFormatAsString)
        Log.i(
            CursorToImageGsonConverter::class.java.name,
            "imageFormat: $imageFormat"
        )

        val image = ImageGson()
        image.id = id
        image.revisionNumber = revisionNumber
        image.title = title
        image.imageFormat = imageFormat

        return image
    }
}
