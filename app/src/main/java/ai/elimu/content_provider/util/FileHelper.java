package ai.elimu.content_provider.util;

import android.content.Context;

import java.io.File;

import ai.elimu.content_provider.language.SharedPreferencesHelper;
import ai.elimu.model.enums.Language;
import ai.elimu.model.v2.gson.content.ImageGson;
import ai.elimu.model.v2.gson.content.VideoGson;

/**
 * Helper class for determining folder paths of multimedia files.
 */
public class FileHelper {

    private static File getImagesDirectory(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        Language language = SharedPreferencesHelper.getLanguage(context);
        File languageDirectory = new File(externalFilesDir, "lang-" + language.getIsoCode());
        File imagesDirectory = new File(languageDirectory, "images");
        if (!imagesDirectory.exists()) {
            imagesDirectory.mkdirs();
        }
        return imagesDirectory;
    }

    public static File getImageFile(ImageGson imageGson, Context context) {
        if ((imageGson.getId() == null) || (imageGson.getRevisionNumber() == null)) {
            return null;
        }
        File imagesDirectory = getImagesDirectory(context);
        File file = new File(imagesDirectory, imageGson.getId() + "_r" + imageGson.getRevisionNumber() + "." + imageGson.getImageFormat().toString().toLowerCase());
        return file;
    }
    

    private static File getVideosDirectory(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        Language language = SharedPreferencesHelper.getLanguage(context);
        File languageDirectory = new File(externalFilesDir, "lang-" + language.getIsoCode());
        File videosDirectory = new File(languageDirectory, "videos");
        if (!videosDirectory.exists()) {
            videosDirectory.mkdirs();
        }
        return videosDirectory;
    }

    public static File getVideoFile(VideoGson videoGson, Context context) {
        if ((videoGson.getId() == null) || (videoGson.getRevisionNumber() == null)) {
            return null;
        }
        File videosDirectory = getVideosDirectory(context);
        File file = new File(videosDirectory, videoGson.getId() + "_r" + videoGson.getRevisionNumber() + "." + videoGson.getVideoFormat().toString().toLowerCase());
        return file;
    }
}
