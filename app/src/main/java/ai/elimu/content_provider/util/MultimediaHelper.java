package ai.elimu.content_provider.util;

import android.content.Context;

import java.io.File;

import ai.elimu.model.gson.content.multimedia.ImageGson;

/**
 * Helper class for determining folder paths of multimedia content.
 */
public class MultimediaHelper {

    private static File getImagesDirectory(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        File imagesDirectory = new File(externalFilesDir, "images");
        if (!imagesDirectory.exists()) {
            imagesDirectory.mkdirs();
        }
        return imagesDirectory;
    }

    public static File getFile(ImageGson imageGson, Context context) {
        if (imageGson == null) {
            return null;
        }
        File imagesDirectory = getImagesDirectory(context);
        File file = new File(imagesDirectory, imageGson.getId() + "_r" + imageGson.getRevisionNumber() + "." + imageGson.getImageFormat().toString().toLowerCase());
        return file;
    }
}
