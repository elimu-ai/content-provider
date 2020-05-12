package ai.elimu.content_provider.room.db;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import ai.elimu.model.enums.content.ImageFormat;
import ai.elimu.model.enums.content.WordType;

/**
 * See https://developer.android.com/training/data-storage/room/referencing-data
 */
public class Converters {

    @TypeConverter
    public static ImageFormat fromImageFormat(String value) {
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


    @TypeConverter
    public static WordType fromWordType(String value) {
        WordType wordType = null;
        if (!TextUtils.isEmpty(value)) {
            wordType = WordType.valueOf(value);
        }
        return wordType;
    }

    @TypeConverter
    public static String toString(WordType wordType) {
        String value = null;
        if (wordType != null) {
            value = wordType.toString();
        }
        return value;
    }
}
