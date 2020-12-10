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
import ai.elimu.content_provider.room.dao.AudioDao;
import ai.elimu.content_provider.room.db.RoomDb;

public class AudioContentProvider extends ContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.audio_provider";

    private static final String TABLE_AUDIOS = "audios";
    private static final int CODE_AUDIOS = 1;
    private static final int CODE_AUDIO_ID = 2;
    private static final int CODE_AUDIO_TRANSCRIPTION = 3;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_AUDIOS, CODE_AUDIOS);
        MATCHER.addURI(AUTHORITY, TABLE_AUDIOS + "/#", CODE_AUDIO_ID);
        MATCHER.addURI(AUTHORITY, TABLE_AUDIOS + "/by-transcription/*", CODE_AUDIO_TRANSCRIPTION);
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
        AudioDao audioDao = roomDb.audioDao();

        final int code = MATCHER.match(uri);
        Log.i(getClass().getName(), "code: " + code);
        if (code == CODE_AUDIOS) {
            final Cursor cursor;

            // Get the Room Cursor
            cursor = audioDao.loadAllAsCursor();
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_AUDIO_ID) {
            // Extract the Audio ID from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String audioIdAsString = pathSegments.get(1);
            Long audioId = Long.valueOf(audioIdAsString);
            Log.i(getClass().getName(), "audioId: " + audioId);

            final Cursor cursor;

            // Get the Room Cursor
            cursor = audioDao.loadAsCursor(audioId);
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_AUDIO_TRANSCRIPTION) {
            // Extract the transcription from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String transcription = pathSegments.get(2);
            Log.i(getClass().getName(), "transcription: \"" + transcription + "\"");

            final Cursor cursor;

            // Get the Room Cursor
            cursor = audioDao.loadByTranscriptionAsCursor(transcription);
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
