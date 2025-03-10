package ai.elimu.content_provider.utils.converter;

import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;

import ai.elimu.model.v2.enums.content.VideoFormat;
import ai.elimu.model.v2.gson.content.VideoGson;

public class CursorToVideoGsonConverter {

    private static String TAG = CursorToVideoGsonConverter.class.getName();

    public static VideoGson getVideoGson(Cursor cursor) {
        Log.i(TAG, "getVideoGson");

        Log.i(TAG, "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnId);
        Log.i(TAG, "id: " + id);

        int columnRevisionNumber = cursor.getColumnIndex("revisionNumber");
        Integer revisionNumber = cursor.getInt(columnRevisionNumber);
        Log.i(TAG, "revisionNumber: " + revisionNumber);

        int columnTitle = cursor.getColumnIndex("title");
        String title = cursor.getString(columnTitle);
        Log.i(TAG, "title: \"" + title + "\"");

        int columnVideoFormat = cursor.getColumnIndex("videoFormat");
        String videoFormatAsString = cursor.getString(columnVideoFormat);
        Log.i(TAG, "videoFormatAsString: " + videoFormatAsString);
        VideoFormat videoFormat = VideoFormat.valueOf(videoFormatAsString);
        Log.i(TAG, "videoFormat: " + videoFormat);

        VideoGson video = new VideoGson();
        video.setId(id);
        video.setRevisionNumber(revisionNumber);
        video.setTitle(title);
        video.setVideoFormat(videoFormat);

        return video;
    }
}
