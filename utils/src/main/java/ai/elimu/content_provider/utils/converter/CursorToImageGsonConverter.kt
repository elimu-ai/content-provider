package ai.elimu.content_provider.utils.converter;

import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;

import ai.elimu.model.v2.enums.content.ImageFormat;
import ai.elimu.model.v2.gson.content.ImageGson;

public class CursorToImageGsonConverter {

    public static ImageGson getImageGson(Cursor cursor) {
        Log.i(CursorToImageGsonConverter.class.getName(), "getImageGson");

        Log.i(CursorToImageGsonConverter.class.getName(), "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnId);
        Log.i(CursorToImageGsonConverter.class.getName(), "id: " + id);

        int columnRevisionNumber = cursor.getColumnIndex("revisionNumber");
        Integer revisionNumber = cursor.getInt(columnRevisionNumber);
        Log.i(CursorToImageGsonConverter.class.getName(), "revisionNumber: " + revisionNumber);

        int columnTitle = cursor.getColumnIndex("title");
        String title = cursor.getString(columnTitle);
        Log.i(CursorToImageGsonConverter.class.getName(), "title: \"" + title + "\"");

        int columnImageFormat = cursor.getColumnIndex("imageFormat");
        String imageFormatAsString = cursor.getString(columnImageFormat);
        Log.i(CursorToImageGsonConverter.class.getName(), "imageFormatAsString: " + imageFormatAsString);
        ImageFormat imageFormat = ImageFormat.valueOf(imageFormatAsString);
        Log.i(CursorToImageGsonConverter.class.getName(), "imageFormat: " + imageFormat);

        ImageGson image = new ImageGson();
        image.setId(id);
        image.setRevisionNumber(revisionNumber);
        image.setTitle(title);
        image.setImageFormat(imageFormat);

        return image;
    }
}
