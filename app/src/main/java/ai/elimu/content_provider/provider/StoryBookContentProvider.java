package ai.elimu.content_provider.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.List;

import ai.elimu.content_provider.BuildConfig;
import ai.elimu.content_provider.room.dao.StoryBookChapterDao;
import ai.elimu.content_provider.room.dao.StoryBookDao;
import ai.elimu.content_provider.room.db.RoomDb;

public class StoryBookContentProvider extends ContentProvider {

    // The authority of this content provider
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.storybook_provider";

    private static final String TABLE_STORYBOOKS = "storybooks";
    private static final int CODE_STORYBOOKS = 1;
    private static final int CODE_STORYBOOK_ID = 2;
    private static final int CODE_STORYBOOK_CHAPTERS = 3;
    public static final Uri URI_STORYBOOK = Uri.parse("content://" + AUTHORITY + "/" + TABLE_STORYBOOKS);

    // The URI matcher
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_STORYBOOKS, CODE_STORYBOOKS);
        MATCHER.addURI(AUTHORITY, TABLE_STORYBOOKS + "/#", CODE_STORYBOOK_ID);
        MATCHER.addURI(AUTHORITY, TABLE_STORYBOOKS + "/#/chapters", CODE_STORYBOOK_CHAPTERS);
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

        Context context = getContext();
        Log.i(getClass().getName(), "context: " + context);
        if (context == null) {
            return null;
        }

        RoomDb roomDb = RoomDb.getDatabase(context);
        StoryBookDao storyBookDao = roomDb.storyBookDao();
        StoryBookChapterDao storyBookChapterDao = roomDb.storyBookChapterDao();

        final int code = MATCHER.match(uri);
        Log.i(getClass().getName(), "code: " + code);
        if (code == CODE_STORYBOOKS) {
            final Cursor cursor;

            // Get the Room Cursor
            cursor = storyBookDao.loadAllAsCursor();
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_STORYBOOK_ID) {
            // Extract the StoryBook ID from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String storyBookIdAsString = pathSegments.get(1);
            Long storyBookId = Long.valueOf(storyBookIdAsString);
            Log.i(getClass().getName(), "storyBookId: " + storyBookId);

            final Cursor cursor;

            // Get the Room Cursor
            cursor = storyBookDao.loadAsCursor(storyBookId);
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_STORYBOOK_CHAPTERS) {
            // Extract the StoryBook ID from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String storyBookIdAsString = pathSegments.get(1);
            Long storyBookId = Long.valueOf(storyBookIdAsString);
            Log.i(getClass().getName(), "storyBookId: " + storyBookId);

            final Cursor cursor;

            // Get the Room Cursor
            cursor = storyBookChapterDao.loadAllAsCursor(storyBookId);
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
}
