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
import ai.elimu.content_provider.room.dao.WordDao;
import ai.elimu.content_provider.room.db.RoomDb;

public class WordContentProvider extends ContentProvider {

    // The authority of this content provider
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.word_provider";

    private static final String TABLE_WORDS = "words";
    private static final int CODE_WORDS = 1;
    private static final int CODE_WORD_ID = 2;
    private static final int CODE_WORDS_BY_STORYBOOK_PARAGRAPH_ID = 3;
    public static final Uri URI_WORD = Uri.parse("content://" + AUTHORITY + "/" + TABLE_WORDS);

    // The URI matcher
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_WORDS, CODE_WORDS);
        MATCHER.addURI(AUTHORITY, TABLE_WORDS + "/#", CODE_WORD_ID);
        MATCHER.addURI(AUTHORITY, TABLE_WORDS + "/by-paragraph-id/#", CODE_WORDS_BY_STORYBOOK_PARAGRAPH_ID);
    }

    @Override
    public boolean onCreate() {
        Log.i(getClass().getName(), "onCreate");

        Log.i(getClass().getName(), "URI_WORD: " + URI_WORD);

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
        WordDao wordDao = roomDb.wordDao();

        final int code = MATCHER.match(uri);
        Log.i(getClass().getName(), "code: " + code);
        if (code == CODE_WORDS) {
            final Cursor cursor;

            // Get the Room Cursor
            cursor = wordDao.loadAllAsCursor();
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_WORDS_BY_STORYBOOK_PARAGRAPH_ID) {
            // Extract the StoryBookParagraph ID from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String storyBookParagraphIdAsString = pathSegments.get(2);
            Long storyBookParagraphId = Long.valueOf(storyBookParagraphIdAsString);
            Log.i(getClass().getName(), "storyBookParagraphId: " + storyBookParagraphId);

            final Cursor cursor;

            // Get the Room Cursor
            cursor = wordDao.loadAllAsCursor(storyBookParagraphId);
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_WORD_ID) {
            // Extract the Word ID from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String wordIdAsString = pathSegments.get(1);
            Long wordId = Long.valueOf(wordIdAsString);
            Log.i(getClass().getName(), "wordId: " + wordId);

            final Cursor cursor;

            // Get the Room Cursor
            cursor = wordDao.loadAsCursor(wordId);
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
