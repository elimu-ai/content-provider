package ai.elimu.content_provider.provider

import ai.elimu.content_provider.BuildConfig
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.content_provider.util.FileHelper
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.FileNotFoundException

class VideoContentProvider : ContentProvider() {
    
    private val TAG = javaClass.name
    
    override fun onCreate(): Boolean {
        Log.i(TAG, "onCreate")

        return true
    }

    /**
     * Handles query requests from clients.
     */
    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        Log.i(TAG, "query")

        Log.i(TAG, "uri: $uri")
        Log.i(TAG, "projection: $projection")
        Log.i(TAG, "selection: $selection")
        Log.i(TAG, "selectionArgs: $selectionArgs")
        Log.i(TAG, "sortOrder: $sortOrder")

        val context = context
        Log.i(TAG, "context: $context")
        if (context == null) {
            return null
        }

        val roomDb = RoomDb.getDatabase(context)
        val videoDao = roomDb.videoDao()

        val code = MATCHER.match(uri)
        Log.i(TAG, "code: $code")
        when (code) {
            CODE_VIDEOS -> {
                // Get the Room Cursor
                val cursor = videoDao.loadAllAsCursor()
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                return cursor
            }
            CODE_VIDEO_ID -> {
                // Extract the Video ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(TAG, "pathSegments: $pathSegments")
                val videoIdAsString = pathSegments[1]
                val videoId = videoIdAsString.toLong()
                Log.i(TAG, "videoId: $videoId")

                // Get the Room Cursor
                val cursor = videoDao.loadAsCursor(videoId)
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                return cursor
            }
            CODE_VIDEO_TITLE -> {
                // Extract the transcription from the URI
                val pathSegments = uri.pathSegments
                Log.i(TAG, "pathSegments: $pathSegments")
                val title = pathSegments[2]
                Log.i(TAG, "title: \"$title\"")

                // Get the Room Cursor
                val cursor = videoDao.loadByTitleAsCursor(title)
                Log.i(TAG, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                return cursor
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    /**
     * Handles requests for the MIME type of the data at the given URI.
     */
    override fun getType(uri: Uri): String? {
        Log.i(TAG, "getType")

        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handles requests to insert a new row.
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.i(TAG, "insert")

        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handles requests to update one or more rows.
     */
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        Log.i(TAG, "update")

        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handle requests to delete one or more rows.
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.i(TAG, "delete")

        throw UnsupportedOperationException("Not yet implemented")
    }

    @Throws(FileNotFoundException::class)
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val segments = uri.pathSegments
        if (segments.size < 2) {
            throw FileNotFoundException("Invalid URI: $uri")
        }
        val fileId = segments[1]

        val roomDb = RoomDb.getDatabase(context)
        val videoDao = roomDb.videoDao()
        val video = videoDao.load(fileId.toLong()) ?: throw FileNotFoundException("File not found!")

        val videoFile = FileHelper.getVideoFile(video, context)
            ?: throw FileNotFoundException("File not found!")
        if (!videoFile.exists()) {
            Log.e("VideoContentProvider", "videoFile doesn't exist: " + videoFile.absolutePath)
            throw FileNotFoundException("File not found: " + videoFile.absolutePath)
        }
        return ParcelFileDescriptor.open(videoFile, ParcelFileDescriptor.MODE_READ_ONLY)
    }

    companion object {
        private const val AUTHORITY: String = BuildConfig.APPLICATION_ID + ".provider.video_provider"

        private const val TABLE_VIDEOS = "videos"
        private const val CODE_VIDEOS = 1
        private const val CODE_VIDEO_ID = 2
        private const val CODE_VIDEO_TITLE = 4

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_VIDEOS, CODE_VIDEOS)
            MATCHER.addURI(AUTHORITY, "$TABLE_VIDEOS/#", CODE_VIDEO_ID)
            MATCHER.addURI(AUTHORITY, "$TABLE_VIDEOS/by-title/*", CODE_VIDEO_TITLE)
        }
    }
}
