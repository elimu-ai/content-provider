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

        val columnId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnId)
        Log.i(TAG, "id: $id")

        val columnRevisionNumber = cursor.getColumnIndex("revisionNumber")
        val revisionNumber = cursor.getInt(columnRevisionNumber)
        Log.i(TAG, "revisionNumber: $revisionNumber")

        val columnTitle = cursor.getColumnIndex("title")
        val title = cursor.getString(columnTitle)
        Log.i(TAG, "title: \"$title\"")

        val columnVideoFormat = cursor.getColumnIndex("videoFormat")
        val videoFormatAsString = cursor.getString(columnVideoFormat)
        Log.i(TAG, "videoFormatAsString: $videoFormatAsString")
        val videoFormat = VideoFormat.valueOf(videoFormatAsString)
        Log.i(TAG, "videoFormat: $videoFormat")

        val video = VideoGson()
        video.id = id
        video.revisionNumber = revisionNumber
        video.title = title
        video.videoFormat = videoFormat

        return video
    }
}
