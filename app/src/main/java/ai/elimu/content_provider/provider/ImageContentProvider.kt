package ai.elimu.content_provider.provider

import ai.elimu.content_provider.BuildConfig
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.content_provider.room.entity.Image
import ai.elimu.content_provider.util.FileHelper.getImageFile
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.FileNotFoundException

class ImageContentProvider : ContentProvider() {
    private val TAG: String = ImageContentProvider::class.java.name

    override fun onCreate(): Boolean {
        Log.i(javaClass.name, "onCreate")

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
        Log.i(javaClass.name, "query")

        Log.i(javaClass.name, "uri: $uri")
        Log.i(javaClass.name, "projection: $projection")
        Log.i(javaClass.name, "selection: $selection")
        Log.i(javaClass.name, "selectionArgs: $selectionArgs")
        Log.i(javaClass.name, "sortOrder: $sortOrder")

        val context = context
        Log.i(javaClass.name, "context: $context")
        if (context == null) {
            return null
        }

        val roomDb = RoomDb.getDatabase(context)
        val imageDao = roomDb.imageDao()

        val code = MATCHER.match(uri)
        Log.i(javaClass.name, "code: $code")
        when (code) {
            CODE_IMAGES -> {
                // Get the Room Cursor
                val cursor = imageDao.loadAllAsCursor()
                Log.i(javaClass.name, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                // Set DB column names needed by the Cursor-to-Gson converter in the :utils module
                val bundle = Bundle().apply {
                    putInt("version_code", BuildConfig.VERSION_CODE)
                    putString("id", Image::id.name)
                    putString("revision_number", Image::revisionNumber.name)
                    putString("usage_count", Image::usageCount.name)
                    putString("title", Image::title.name)
                    putString("image_format", Image::imageFormat.name)
                    putString("checksum_md5", Image::checksumMd5.name)
                }
                cursor.extras = bundle

                return cursor
            }
            CODE_IMAGE_ID -> {
                // Extract the Image ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(javaClass.name, "pathSegments: $pathSegments")
                val imageIdAsString = pathSegments[1]
                val imageId = imageIdAsString.toLong()
                Log.i(javaClass.name, "imageId: $imageId")

                // Get the Room Cursor
                val cursor = imageDao.loadAsCursor(imageId)
                Log.i(javaClass.name, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                // Set DB column names needed by the Cursor-to-Gson converter in the :utils module
                val bundle = Bundle().apply {
                    putInt("version_code", BuildConfig.VERSION_CODE)
                    putString("id", Image::id.name)
                    putString("revision_number", Image::revisionNumber.name)
                    putString("usage_count", Image::usageCount.name)
                    putString("title", Image::title.name)
                    putString("image_format", Image::imageFormat.name)
                    putString("checksum_md5", Image::checksumMd5.name)
                }
                cursor.extras = bundle

                return cursor
            }
            CODE_IMAGES_BY_WORD_LABEL_ID -> {
                // Extract the Word ID from the URI
                val pathSegments = uri.pathSegments
                Log.i(javaClass.name, "pathSegments: $pathSegments")
                val wordIdAsString = pathSegments[2]
                val wordId = wordIdAsString.toLong()
                Log.i(javaClass.name, "wordId: $wordId")

                // Get the Room Cursor
                val cursor = imageDao.loadAllByWordLabelAsCursor(wordId)
                Log.i(javaClass.name, "cursor: $cursor")

                cursor.setNotificationUri(context.contentResolver, uri)

                // Set DB column names needed by the Cursor-to-Gson converter in the :utils module
                val bundle = Bundle().apply {
                    putInt("version_code", BuildConfig.VERSION_CODE)
                    putString("id", Image::id.name)
                    putString("revision_number", Image::revisionNumber.name)
                    putString("usage_count", Image::usageCount.name)
                    putString("title", Image::title.name)
                    putString("image_format", Image::imageFormat.name)
                    putString("checksum_md5", Image::checksumMd5.name)
                }
                cursor.extras = bundle

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
        Log.i(javaClass.name, "getType")

        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handles requests to insert a new row.
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.i(javaClass.name, "insert")

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
        Log.i(javaClass.name, "update")

        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handle requests to delete one or more rows.
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.i(javaClass.name, "delete")

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
        val imageDao = roomDb.imageDao()

        val imageId: Long
        try {
            imageId = fileId.toLong()
        } catch (e: NumberFormatException) {
            Log.e(TAG, "Failed to parse image ID: $fileId", e)
            throw FileNotFoundException("Invalid image ID format: $fileId")
        }

        val image = imageDao.load(imageId)
            ?: throw FileNotFoundException("File not found with id: $imageId")

        val imageFile = getImageFile(image, context)
            ?: throw FileNotFoundException("imageFile not found with id: $imageId")
        if (!imageFile.exists()) {
            Log.e(TAG, "imageFile doesn't exist: " + imageFile.absolutePath)
            throw FileNotFoundException("File not found: " + imageFile.absolutePath)
        }
        return ParcelFileDescriptor.open(imageFile, ParcelFileDescriptor.MODE_READ_ONLY)
    }

    companion object {
        private const val AUTHORITY: String = BuildConfig.APPLICATION_ID + ".provider.image_provider"

        private const val TABLE_IMAGES = "images"
        private const val CODE_IMAGES = 1
        private const val CODE_IMAGE_ID = 2
        private const val CODE_IMAGES_BY_WORD_LABEL_ID = 3

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_IMAGES, CODE_IMAGES)
            MATCHER.addURI(AUTHORITY, "$TABLE_IMAGES/#", CODE_IMAGE_ID)
            MATCHER.addURI(
                AUTHORITY,
                "$TABLE_IMAGES/by-word-label-id/#",
                CODE_IMAGES_BY_WORD_LABEL_ID
            )
        }
    }
}
