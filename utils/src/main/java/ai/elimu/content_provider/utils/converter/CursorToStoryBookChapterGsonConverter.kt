package ai.elimu.content_provider.utils.converter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.elimu.model.v2.gson.content.ImageGson;
import ai.elimu.model.v2.gson.content.StoryBookChapterGson;
import ai.elimu.model.v2.gson.content.StoryBookParagraphGson;

public class CursorToStoryBookChapterGsonConverter {

    public static StoryBookChapterGson getStoryBookChapterGson(Cursor cursor, Context context, String contentProviderApplicationId) {
        Log.i(CursorToStoryBookChapterGsonConverter.class.getName(), "getStoryBookChapterGson");

        Log.i(CursorToStoryBookChapterGsonConverter.class.getName(), "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnId);
        Log.i(CursorToStoryBookChapterGsonConverter.class.getName(), "id: " + id);

        int columnSortOrder = cursor.getColumnIndex("sortOrder");
        Integer sortOrder = cursor.getInt(columnSortOrder);
        Log.i(CursorToStoryBookChapterGsonConverter.class.getName(), "sortOrder: " + sortOrder);

        ImageGson imageGson = null;
        int columnImageId = cursor.getColumnIndex("imageId");
        Long imageId = cursor.getLong(columnImageId);
        Log.i(CursorToImageGsonConverter.class.getName(), "imageId: " + imageId);
        if (imageId != null) {
            Uri imageUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.image_provider/images/" + imageId);
            Log.i(CursorToImageGsonConverter.class.getName(), "imageUri: " + imageUri);
            Cursor imageCursor = context.getContentResolver().query(imageUri, null, null, null, null);
            if (imageCursor == null) {
                Log.e(CursorToImageGsonConverter.class.getName(), "imageCursor == null");
                Toast.makeText(context, "imageCursor == null", Toast.LENGTH_LONG).show();
            } else {
                Log.i(CursorToImageGsonConverter.class.getName(), "imageCursor.getCount(): " + imageCursor.getCount());
                if (imageCursor.getCount() == 0) {
                    Log.e(CursorToImageGsonConverter.class.getName(), "imageCursor.getCount() == 0");
                } else {
                    Log.i(CursorToImageGsonConverter.class.getName(), "imageCursor.getCount(): " + imageCursor.getCount());

                    imageCursor.moveToFirst();

                    // Convert from Room to Gson
                    imageGson = CursorToImageGsonConverter.getImageGson(imageCursor);

                    imageCursor.close();
                    Log.i(CursorToImageGsonConverter.class.getName(), "imageCursor.isClosed(): " + imageCursor.isClosed());
                }
            }
        }

        List<StoryBookParagraphGson> paragraphGsons = null;
        Uri paragraphsUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.storybook_provider/storybooks/0/chapters/" + id + "/paragraphs");
        Log.i(CursorToImageGsonConverter.class.getName(), "paragraphsUri: " + paragraphsUri);
        Cursor paragraphsCursor = context.getContentResolver().query(paragraphsUri, null, null, null, null);
        if (paragraphsCursor == null) {
            Log.e(CursorToImageGsonConverter.class.getName(), "paragraphsCursor == null");
            Toast.makeText(context, "paragraphsCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(CursorToImageGsonConverter.class.getName(), "paragraphsCursor.getCount(): " + paragraphsCursor.getCount());
            if (paragraphsCursor.getCount() == 0) {
                Log.e(CursorToImageGsonConverter.class.getName(), "paragraphsCursor.getCount() == 0");
            } else {
                Log.i(CursorToImageGsonConverter.class.getName(), "paragraphsCursor.getCount(): " + paragraphsCursor.getCount());

                paragraphGsons = new ArrayList<>();

                boolean isLast = false;
                while (!isLast) {
                    paragraphsCursor.moveToNext();

                    // Convert from Room to Gson
                    StoryBookParagraphGson storyBookParagraphGson = CursorToStoryBookParagraphGsonConverter.getStoryBookParagraphGson(paragraphsCursor, context, contentProviderApplicationId);
                    paragraphGsons.add(storyBookParagraphGson);

                    isLast = paragraphsCursor.isLast();
                }

                paragraphsCursor.close();
                Log.i(CursorToImageGsonConverter.class.getName(), "paragraphsCursor.isClosed(): " + paragraphsCursor.isClosed());
            }
        }

        StoryBookChapterGson storyBookChapter = new StoryBookChapterGson();
        storyBookChapter.setId(id);
        storyBookChapter.setSortOrder(sortOrder);
        storyBookChapter.setImage(imageGson);
        storyBookChapter.setStoryBookParagraphs(paragraphGsons);

        return storyBookChapter;
    }
}
