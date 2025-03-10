package ai.elimu.content_provider.utils.converter

import ai.elimu.model.v2.enums.content.VideoFormat
import ai.elimu.model.v2.gson.content.VideoGson
import android.database.Cursor
import android.util.Log

object CursorToVideoGsonConverter {
    private val TAG: String = CursorToVideoGsonConverter::class.java.name

    fun getVideoGson(cursor: Cursor): VideoGson {
        Log.i(TAG, "getVideoGson")
        Log.i(TAG, "Arrays.toString(cursor.getColumnNames()): "
                + cursor.columnNames.contentToString())
        if (cursor.isBeforeFirst && !cursor.moveToFirst()) {
            throw IllegalArgumentException("Cursor must be positioned on a valid row")
        }
        val columnId = cursor.getColumnIndex("id")
        if (columnId == -1) {
            throw IllegalArgumentException("Column 'id' not found in cursor")
        }
        val id = cursor.getLong(columnId)
        Log.i(TAG, "id: $id")

        val columnRevisionNumber = cursor.getColumnIndex("revisionNumber")
        if (columnRevisionNumber == -1) {
            throw IllegalArgumentException("Column 'revisionNumber' not found in cursor")
        }
        val revisionNumber = cursor.getInt(columnRevisionNumber)
        Log.i(TAG, "revisionNumber: $revisionNumber")

        val columnTitle = cursor.getColumnIndex("title")
        if (columnTitle == -1) {
            throw IllegalArgumentException("Column 'title' not found in cursor")
        }
        val title = cursor.getString(columnTitle)
        Log.i(TAG, "title: \"$title\"")

        val columnVideoFormat = cursor.getColumnIndex("videoFormat")
        if (columnVideoFormat == -1) {
            throw IllegalArgumentException("Column 'videoFormat' not found in cursor")
        }
        val videoFormatAsString = cursor.getString(columnVideoFormat)
        Log.i(TAG, "videoFormatAsString: $videoFormatAsString")
        val videoFormat = try {
            VideoFormat.valueOf(videoFormatAsString)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Invalid video format: $videoFormatAsString", e)
            throw IllegalArgumentException("Invalid video format: $videoFormatAsString", e)
        }
        Log.i(TAG, "videoFormat: $videoFormat")
        val video = VideoGson()
        video.id = id
        video.revisionNumber = revisionNumber
        video.title = title
        video.videoFormat = videoFormat
        return video
    }
}
