package ai.elimu.content_provider.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import ai.elimu.content_provider.BuildConfig;
import ai.elimu.content_provider.room.dao.ImageDao;
import ai.elimu.content_provider.room.db.RoomDb;

public class ImageContentProvider extends ContentProvider {

    // The authority of this content provider
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.image_provider";

    private static final String TABLE_IMAGE = "image";
    private static final int CODE_IMAGE_DIR = 3;
    public static final Uri URI_IMAGE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_IMAGE);

    // The URI matcher
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_IMAGE, CODE_IMAGE_DIR);
    }

    @Override
    public boolean onCreate() {
        Log.i(getClass().getName(), "onCreate");

        Log.i(getClass().getName(), "URI_IMAGE: " + URI_IMAGE);

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

        final int code = MATCHER.match(uri);
        if (code != CODE_IMAGE_DIR) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        } else {
            Context context = getContext();
            Log.i(getClass().getName(), "context: " + context);
            if (context == null) {
                return null;
            }

            final Cursor cursor;

            // Get the Room Cursor
            RoomDb roomDb = RoomDb.getDatabase(context);
            ImageDao imageDao = roomDb.imageDao();
            cursor = imageDao.loadAllAsCursor();
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
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
}
