package ai.elimu.content_provider.util

import ai.elimu.content_provider.room.entity.Image
import ai.elimu.content_provider.room.entity.Video
import ai.elimu.model.v2.gson.content.ImageGson
import ai.elimu.model.v2.gson.content.VideoGson
import android.content.Context
import android.os.Environment
import androidx.annotation.RestrictTo
import java.io.File
import java.util.Locale

/**
 * Helper class for determining folder paths of multimedia files.
 */
object FileHelper {
    fun getImageFile(imageGson: ImageGson, context: Context?): File? {
        context ?: return null
        if ((imageGson.id == null) || (imageGson.revisionNumber == null)) {
            return null
        }
        val imagesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val fileName = imageGson.checksumMd5
        return File(
            imagesDirectory, (fileName + "."
                    + imageGson.imageFormat.toString().lowercase(Locale.getDefault()))
        )
    }

    @JvmStatic
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
    fun getImageFile(imageGson: Image, context: Context?): File? {
        context ?: return null
        if ((imageGson.id == null) || (imageGson.revisionNumber == null)) {
            return null
        }
        val imagesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(
            imagesDirectory, (imageGson.checksumMd5 + "."
                    + imageGson.imageFormat.toString().lowercase(Locale.getDefault()))
        )
    }

    @JvmStatic
    fun getVideoFile(videoGson: VideoGson, context: Context?): File? {
        context ?: return null
        if ((videoGson.id == null) || (videoGson.revisionNumber == null)) {
            return null
        }
        val videosDirectory = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        val file = File(
            videosDirectory,
            videoGson.id.toString() + "_r" + videoGson.revisionNumber + "." + videoGson.videoFormat.toString()
                .lowercase(
                    Locale.getDefault()
                )
        )
        return file
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
    fun getVideoFile(videoGson: Video, context: Context?): File? {
        context ?: return null
        if ((videoGson.id == null) || (videoGson.revisionNumber == null)) {
            return null
        }
        val videosDirectory = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        return File(
            videosDirectory, (videoGson.id
                .toString() + "_r" + videoGson.revisionNumber + "."
                    + videoGson.videoFormat.toString().lowercase(Locale.getDefault()))
        )
    }
}
