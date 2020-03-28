package ai.elimu.content_provider.room.db;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import ai.elimu.model.enums.content.ImageFormat;

/**
 * See https://developer.android.com/training/data-storage/room/referencing-data
 */
public class Converters {

    @TypeConverter
    public static ImageFormat fromString(String value) {
        ImageFormat imageFormat = null;
        if (!TextUtils.isEmpty(value)) {
            imageFormat = ImageFormat.valueOf(value);
        }
        return imageFormat;
    }

    @TypeConverter
    public static String toString(ImageFormat imageFormat) {
        String value = imageFormat.toString();
        return value;
    }
}
