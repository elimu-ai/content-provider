package ai.elimu.content_provider.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import ai.elimu.content_provider.BuildConfig;
import ai.elimu.content_provider.room.dao.StoryBookDao;
import ai.elimu.content_provider.room.db.RoomDb;

public class StoryBookContentProvider extends ContentProvider {

    // The authority of this content provider
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.storybook_provider";

    private static final String TABLE_STORYBOOK = "storybook";
    private static final int CODE_STORYBOOK_DIR = 2;
    public static final Uri URI_STORYBOOK = Uri.parse("content://" + AUTHORITY + "/" + TABLE_STORYBOOK);

    // The URI matcher
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_STORYBOOK, CODE_STORYBOOK_DIR);
    }

    @Override
    public boolean onCreate() {
        Log.i(getClass().getName(), "onCreate");

        Log.i(getClass().getName(), "URI_STORYBOOK: " + URI_STORYBOOK);

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
        if (code != CODE_STORYBOOK_DIR) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        } else {
            Context context = getContext();
            Log.i(getClass().getName(), "context: " + context);
            if (context == null) {
                return null;
            }

            final Cursor[] cursor = {null};

            // Get the Room Cursor
            RoomDb roomDb = RoomDb.getDatabase(context);
            RoomDb.databaseWriteExecutor.execute(() -> {
                StoryBookDao storyBookDao = roomDb.storyBookDao();
                cursor[0] = storyBookDao.loadAllAsCursor();
                Log.i(getClass().getName(), "cursor[0]: " + cursor[0]);
            });

            cursor[0].setNotificationUri(context.getContentResolver(), uri);

            return cursor[0];
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
