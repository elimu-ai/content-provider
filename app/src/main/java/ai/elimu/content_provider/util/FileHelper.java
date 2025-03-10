package ai.elimu.content_provider.util;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.RestrictTo;

import java.io.File;

import ai.elimu.content_provider.room.entity.Image;
import ai.elimu.content_provider.room.entity.Video;
import ai.elimu.model.v2.gson.content.ImageGson;
import ai.elimu.model.v2.gson.content.VideoGson;

/**
 * Helper class for determining folder paths of multimedia files.
 */
public class FileHelper {

    public static File getImageFile(ImageGson imageGson, Context context) {
        if ((imageGson.getId() == null) || (imageGson.getRevisionNumber() == null)) {
            return null;
        }
        File imagesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(imagesDirectory, imageGson.getId()
                + "_r" + imageGson.getRevisionNumber() + "."
                + imageGson.getImageFormat().toString().toLowerCase());
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
    public static File getImageFile(Image imageGson, Context context) {
        if ((imageGson.getId() == null) || (imageGson.getRevisionNumber() == null)) {
            return null;
        }
        File imagesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(imagesDirectory, imageGson.getId()
                + "_r" + imageGson.getRevisionNumber() + "."
                + imageGson.getImageFormat().toString().toLowerCase());
    }

    public static File getVideoFile(VideoGson videoGson, Context context) {
        if ((videoGson.getId() == null) || (videoGson.getRevisionNumber() == null)) {
            return null;
        }
        File videosDirectory = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File file = new File(videosDirectory, videoGson.getId() + "_r" + videoGson.getRevisionNumber() + "." + videoGson.getVideoFormat().toString().toLowerCase());
        return file;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
    public static File getVideoFile(Video videoGson, Context context) {
        if ((videoGson.getId() == null) || (videoGson.getRevisionNumber() == null)) {
            return null;
        }
        File videosDirectory = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        return new File(videosDirectory, videoGson.getId()
                + "_r" + videoGson.getRevisionNumber() + "."
                + videoGson.getVideoFormat().toString().toLowerCase());
    }
}
