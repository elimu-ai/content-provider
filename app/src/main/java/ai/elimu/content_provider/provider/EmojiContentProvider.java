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
import ai.elimu.content_provider.room.dao.EmojiDao;
import ai.elimu.content_provider.room.db.RoomDb;

public class EmojiContentProvider extends ContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.emoji_provider";

    private static final String TABLE_EMOJIS = "emojis";
    private static final int CODE_EMOJIS = 1;
    private static final int CODE_EMOJI_ID = 2;
    private static final int CODE_EMOJIS_BY_WORD_LABEL_ID = 3;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_EMOJIS, CODE_EMOJIS);
        MATCHER.addURI(AUTHORITY, TABLE_EMOJIS + "/#", CODE_EMOJI_ID);
        MATCHER.addURI(AUTHORITY, TABLE_EMOJIS + "/by-word-label-id/#", CODE_EMOJIS_BY_WORD_LABEL_ID);
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
        EmojiDao emojiDao = roomDb.emojiDao();

        final int code = MATCHER.match(uri);
        Log.i(getClass().getName(), "code: " + code);
        if (code == CODE_EMOJIS) {
            final Cursor cursor;

            // Get the Room Cursor
            cursor = emojiDao.loadAllAsCursor();
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_EMOJI_ID) {
            // Extract the Emoji ID from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String emojiIdAsString = pathSegments.get(1);
            Long emojiId = Long.valueOf(emojiIdAsString);
            Log.i(getClass().getName(), "emojiId: " + emojiId);

            final Cursor cursor;

            // Get the Room Cursor
            cursor = emojiDao.loadAsCursor(emojiId);
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_EMOJIS_BY_WORD_LABEL_ID) {
            // Extract the Word ID from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String wordIdAsString = pathSegments.get(2);
            Long wordId = Long.valueOf(wordIdAsString);
            Log.i(getClass().getName(), "wordId: " + wordId);

            final Cursor cursor;

            // Get the Room Cursor
            cursor = emojiDao.loadAllByWordLabelAsCursor(wordId);
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
