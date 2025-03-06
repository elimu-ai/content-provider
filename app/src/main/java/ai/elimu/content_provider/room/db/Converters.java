package ai.elimu.content_provider.room.db;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import ai.elimu.model.v2.enums.ReadingLevel;
import ai.elimu.model.v2.enums.content.ImageFormat;
import ai.elimu.model.v2.enums.content.VideoFormat;
import ai.elimu.model.v2.enums.content.WordType;

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
    public static VideoFormat fromVideoFormat(String value) {
        VideoFormat videoFormat = null;
        if (!TextUtils.isEmpty(value)) {
            videoFormat = VideoFormat.valueOf(value);
        }
        return videoFormat;
    }

    @TypeConverter
    public static String toString(VideoFormat videoFormat) {
        String value = videoFormat.toString();
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


    @TypeConverter
    public static ReadingLevel fromReadingLevel(String value) {
        ReadingLevel readingLevel = null;
        if (!TextUtils.isEmpty(value)) {
            readingLevel = ReadingLevel.valueOf(value);
        }
        return readingLevel;
    }

    @TypeConverter
    public static String toString(ReadingLevel readingLevel) {
        String value = null;
        if (readingLevel != null) {
            value = readingLevel.toString();
        }
        return value;
    }
}
