package ai.elimu.content_provider.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import ai.elimu.content_provider.BuildConfig;
import ai.elimu.content_provider.room.dao.ImageDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.Image;
import ai.elimu.content_provider.util.FileHelper;

public class ImageContentProvider extends ContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.image_provider";

    private static final String TABLE_IMAGES = "images";
    private static final int CODE_IMAGES = 1;
    private static final int CODE_IMAGE_ID = 2;
    private static final int CODE_IMAGES_BY_WORD_LABEL_ID = 3;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private String TAG = ImageContentProvider.class.getName();

    static {
        MATCHER.addURI(AUTHORITY, TABLE_IMAGES, CODE_IMAGES);
        MATCHER.addURI(AUTHORITY, TABLE_IMAGES + "/#", CODE_IMAGE_ID);
        MATCHER.addURI(AUTHORITY, TABLE_IMAGES + "/by-word-label-id/#", CODE_IMAGES_BY_WORD_LABEL_ID);
    }

    @Override
    public boolean onCreate() {
        Log.i(getClass().getName(), "onCreate");

        return true;
    }

    /**
     * Handles query requests from clients.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.i(getClass().getName(), "query");

        Log.i(getClass().getName(), "uri: " + uri);
        Log.i(getClass().getName(), "projection: " + projection);
        Log.i(getClass().getName(), "selection: " + selection);
        Log.i(getClass().getName(), "selectionArgs: " + selectionArgs);
        Log.i(getClass().getName(), "sortOrder: " + sortOrder);

        Context context = getContext();
        Log.i(getClass().getName(), "context: " + context);
        if (context == null) {
            return null;
        }

        RoomDb roomDb = RoomDb.getDatabase(context);
        ImageDao imageDao = roomDb.imageDao();

        final int code = MATCHER.match(uri);
        Log.i(getClass().getName(), "code: " + code);
        if (code == CODE_IMAGES) {
            final Cursor cursor;

            // Get the Room Cursor
            cursor = imageDao.loadAllAsCursor();
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_IMAGE_ID) {
            // Extract the Image ID from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String imageIdAsString = pathSegments.get(1);
            Long imageId = Long.valueOf(imageIdAsString);
            Log.i(getClass().getName(), "imageId: " + imageId);

            final Cursor cursor;

            // Get the Room Cursor
            cursor = imageDao.loadAsCursor(imageId);
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_IMAGES_BY_WORD_LABEL_ID) {
            // Extract the Word ID from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String wordIdAsString = pathSegments.get(2);
            Long wordId = Long.valueOf(wordIdAsString);
            Log.i(getClass().getName(), "wordId: " + wordId);

            final Cursor cursor;

            // Get the Room Cursor
            cursor = imageDao.loadAllByWordLabelAsCursor(wordId);
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    /**
     * Handles requests for the MIME type of the data at the given URI.
     */
    @Override
    public String getType(Uri uri) {
        Log.i(getClass().getName(), "getType");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handles requests to insert a new row.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(getClass().getName(), "insert");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handles requests to update one or more rows.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i(getClass().getName(), "update");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle requests to delete one or more rows.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i(getClass().getName(), "delete");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        List<String> segments = uri.getPathSegments();
        if (segments.size() < 2) {
            throw new FileNotFoundException("Invalid URI: " + uri);
        }
        String fileId = segments.get(1);

        RoomDb roomDb = RoomDb.getDatabase(getContext());
        ImageDao imageDao = roomDb.imageDao();
        
        long imageId;
        try {
            imageId = Long.parseLong(fileId);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Failed to parse image ID: " + fileId, e);
            throw new FileNotFoundException("Invalid image ID format: " + fileId);
        }

        Image image = imageDao.load(imageId);

        if (image == null) {
            throw new FileNotFoundException("File not found with id: " + imageId);
        }

        File imageFile = FileHelper.getImageFile(image, getContext());
        if (imageFile == null) {
            throw new FileNotFoundException("imageFile not found with id: " + imageId);
        }
        if (!imageFile.exists()) {
            Log.e(TAG, "imageFile doesn't exist: " + imageFile.getAbsolutePath());
            throw new FileNotFoundException("File not found: " + imageFile.getAbsolutePath());
        }
        return ParcelFileDescriptor.open(imageFile, ParcelFileDescriptor.MODE_READ_ONLY);
    }

}
