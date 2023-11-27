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
import ai.elimu.content_provider.room.dao.LetterSoundDao;
import ai.elimu.content_provider.room.db.RoomDb;

public class LetterSoundContentProvider extends ContentProvider {

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.letter_sound_provider";
    private static final String TABLE_LETTER_SOUNDS = "letter_sounds";
    private static final int CODE_LETTER_SOUNDS = 1;
    private static final int CODE_LETTER_SOUND_ID = 2;
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_LETTER_SOUNDS, CODE_LETTER_SOUNDS);
        MATCHER.addURI(AUTHORITY, TABLE_LETTER_SOUNDS + "/#", CODE_LETTER_SOUND_ID);
    }

    @Override
    public boolean onCreate() {
        Log.i(getClass().getName(), "onCreate");
        return true;
    }

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
        LetterSoundDao letterSoundDao = roomDb.letterSoundDao();

        final int code = MATCHER.match(uri);
        Log.i(getClass().getName(), "code: " + code);
        if (code == CODE_LETTER_SOUNDS) {
            final Cursor cursor;

            // Get the Room Cursor
            cursor = letterSoundDao.loadAll_Cursor();
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_LETTER_SOUND_ID) {
            // Extract the LetterSound ID from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String idAsString = pathSegments.get(1);
            Long id = Long.valueOf(idAsString);
            Log.i(getClass().getName(), "id: " + id);

            final Cursor cursor;

            // Get the Room Cursor
            cursor = letterSoundDao.load_Cursor(id);
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        Log.i(getClass().getName(), "getType");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(getClass().getName(), "insert");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i(getClass().getName(), "update");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i(getClass().getName(), "delete");

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
