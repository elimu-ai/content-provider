package ai.elimu.content_provider.room.db

import ai.elimu.model.v2.enums.ReadingLevel
import ai.elimu.model.v2.enums.content.ImageFormat
import ai.elimu.model.v2.enums.content.VideoFormat
import ai.elimu.model.v2.enums.content.WordType
import android.text.TextUtils
import androidx.room.TypeConverter

/**
 * See [Referencing complex data using Room](https://developer.android.com/training/data-storage/room/referencing-data)
 */
object Converters {
    @JvmStatic
    @TypeConverter
    fun fromImageFormat(value: String?): ImageFormat? {
        var imageFormat: ImageFormat? = null
        if (!TextUtils.isEmpty(value)) {
            imageFormat = ImageFormat.valueOf(value!!)
        }
        return imageFormat
    }

    @JvmStatic
    @TypeConverter
    fun toString(imageFormat: ImageFormat): String {
        return imageFormat.toString()
    }


    @JvmStatic
    @TypeConverter
    fun fromVideoFormat(value: String?): VideoFormat? {
        var videoFormat: VideoFormat? = null
        if (!TextUtils.isEmpty(value)) {
            videoFormat = VideoFormat.valueOf(value!!)
        }
        return videoFormat
    }

    @JvmStatic
    @TypeConverter
    fun toString(videoFormat: VideoFormat): String {
        return videoFormat.toString()
    }


    @JvmStatic
    @TypeConverter
    fun fromWordType(value: String?): WordType? {
        var wordType: WordType? = null
        if (!TextUtils.isEmpty(value)) {
            wordType = WordType.valueOf(value!!)
        }
        return wordType
    }

    @JvmStatic
    @TypeConverter
    fun toString(wordType: WordType?): String? {
        var value: String? = null
        if (wordType != null) {
            value = wordType.toString()
        }
        return value
    }


    @JvmStatic
    @TypeConverter
    fun fromReadingLevel(value: String?): ReadingLevel? {
        var readingLevel: ReadingLevel? = null
        if (!TextUtils.isEmpty(value)) {
            readingLevel = ReadingLevel.valueOf(value!!)
        }
        return readingLevel
    }

    @JvmStatic
    @TypeConverter
    fun toString(readingLevel: ReadingLevel?): String? {
        var value: String? = null
        if (readingLevel != null) {
            value = readingLevel.toString()
        }
        return value
    }
}
