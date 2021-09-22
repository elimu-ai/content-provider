package ai.elimu.content_provider.utils.converter;

import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;

import ai.elimu.model.v2.enums.content.AudioFormat;
import ai.elimu.model.v2.gson.content.AudioGson;

public class CursorToAudioGsonConverter {

    public static AudioGson getAudioGson(Cursor cursor) {
        Log.i(CursorToAudioGsonConverter.class.getName(), "getAudioGson");

        Log.i(CursorToAudioGsonConverter.class.getName(), "Arrays.toString(cursor.getColumnNames()): " + Arrays.toString(cursor.getColumnNames()));

        int columnId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnId);
        Log.i(CursorToAudioGsonConverter.class.getName(), "id: " + id);

        int columnRevisionNumber = cursor.getColumnIndex("revisionNumber");
        Integer revisionNumber = cursor.getInt(columnRevisionNumber);
        Log.i(CursorToAudioGsonConverter.class.getName(), "revisionNumber: " + revisionNumber);

        int columnTitle = cursor.getColumnIndex("title");
        String title = cursor.getString(columnTitle);
        Log.i(CursorToAudioGsonConverter.class.getName(), "title: \"" + title + "\"");

        int columnTranscription = cursor.getColumnIndex("transcription");
        String transcription = cursor.getString(columnTranscription);
        Log.i(CursorToAudioGsonConverter.class.getName(), "transcription: \"" + transcription + "\"");

        int columnAudioFormat = cursor.getColumnIndex("audioFormat");
        String audioFormatAsString = cursor.getString(columnAudioFormat);
        Log.i(CursorToAudioGsonConverter.class.getName(), "audioFormatAsString: " + audioFormatAsString);
        AudioFormat audioFormat = AudioFormat.valueOf(audioFormatAsString);
        Log.i(CursorToAudioGsonConverter.class.getName(), "audioFormat: " + audioFormat);

        AudioGson audio = new AudioGson();
        audio.setId(id);
        audio.setRevisionNumber(revisionNumber);
        audio.setTitle(title);
        audio.setTranscription(transcription);
        audio.setAudioFormat(audioFormat);

        return audio;
    }
}
