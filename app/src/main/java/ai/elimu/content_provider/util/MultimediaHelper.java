package ai.elimu.content_provider.util;

import android.content.Context;

import java.io.File;

import ai.elimu.content_provider.room.entity.Image;

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

    public static File getFile(Image image, Context context) {
        if (image == null) {
            return null;
        }
        File imagesDirectory = getImagesDirectory(context);
        File file = new File(imagesDirectory, image.getId() + "_r" + image.getRevisionNumber() + "." + image.getImageFormat().toString().toLowerCase());
        return file;
    }
}
