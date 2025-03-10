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
import ai.elimu.content_provider.room.dao.VideoDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.Video;
import ai.elimu.content_provider.util.FileHelper;

public class VideoContentProvider extends ContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.video_provider";

    private static final String TABLE_VIDEOS = "videos";
    private static final int CODE_VIDEOS = 1;
    private static final int CODE_VIDEO_ID = 2;
    private static final int CODE_VIDEO_TITLE = 4;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_VIDEOS, CODE_VIDEOS);
        MATCHER.addURI(AUTHORITY, TABLE_VIDEOS + "/#", CODE_VIDEO_ID);
        MATCHER.addURI(AUTHORITY, TABLE_VIDEOS + "/by-title/*", CODE_VIDEO_TITLE);
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
        VideoDao videoDao = roomDb.videoDao();

        final int code = MATCHER.match(uri);
        Log.i(getClass().getName(), "code: " + code);
        if (code == CODE_VIDEOS) {
            final Cursor cursor;

            // Get the Room Cursor
            cursor = videoDao.loadAllAsCursor();
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_VIDEO_ID) {
            // Extract the Video ID from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String videoIdAsString = pathSegments.get(1);
            Long videoId = Long.valueOf(videoIdAsString);
            Log.i(getClass().getName(), "videoId: " + videoId);

            final Cursor cursor;

            // Get the Room Cursor
            cursor = videoDao.loadAsCursor(videoId);
            Log.i(getClass().getName(), "cursor: " + cursor);

            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else if (code == CODE_VIDEO_TITLE) {
            // Extract the transcription from the URI
            List<String> pathSegments = uri.getPathSegments();
            Log.i(getClass().getName(), "pathSegments: " + pathSegments);
            String title = pathSegments.get(2);
            Log.i(getClass().getName(), "title: \"" + title + "\"");

            final Cursor cursor;

            // Get the Room Cursor
            cursor = videoDao.loadByTitleAsCursor(title);
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
        VideoDao videoDao = roomDb.videoDao();
        Video video = videoDao.load(Long.parseLong(fileId));

        File videoFile = FileHelper.getVideoFile(video, getContext());
        if (videoFile == null) {
            throw new FileNotFoundException("File not found!");
        }
        if (!videoFile.exists()) {
            Log.e("VideoContentProvider", "videoFile doesn't exist: " + videoFile.getAbsolutePath());
            throw new FileNotFoundException("File not found: " + videoFile.getAbsolutePath());
        }
        return ParcelFileDescriptor.open(videoFile, ParcelFileDescriptor.MODE_READ_ONLY);
    }
}
