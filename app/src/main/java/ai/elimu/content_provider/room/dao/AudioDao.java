package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.Audio;

@Dao
public interface AudioDao {

    @Insert
    void insert(Audio audio);

    @Query("SELECT * FROM Audio WHERE id = :id")
    Audio load(Long id);

    @Query("SELECT * FROM Audio WHERE id = :id")
    Cursor loadAsCursor(Long id);

    @Query("SELECT * FROM Audio WHERE transcription = :transcription")
    Audio loadByTranscription(String transcription);

    @Query("SELECT * FROM Audio WHERE transcription = :transcription")
    Cursor loadByTranscriptionAsCursor(String transcription);

    @Query("SELECT * FROM Audio")
    List<Audio> loadAll();

    @Query("SELECT * FROM Audio")
    Cursor loadAllAsCursor();

    @Update
    void update(Audio audio);

    @Query("DELETE FROM Audio")
    void deleteAll();
}
