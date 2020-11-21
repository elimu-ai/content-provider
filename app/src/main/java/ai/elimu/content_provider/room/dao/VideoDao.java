package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.Video;

@Dao
public interface VideoDao {

    @Insert
    void insert(Video video);

    @Query("SELECT * FROM Video WHERE id = :id")
    Video load(Long id);

    @Query("SELECT * FROM Video WHERE id = :id")
    Cursor loadAsCursor(Long id);

    @Query("SELECT * FROM Video")
    List<Video> loadAll();

    @Query("SELECT * FROM Video")
    Cursor loadAllAsCursor();

//    @Query("SELECT * FROM Video i WHERE i.id IN (SELECT Video_id FROM Video_Word WHERE words_id = :wordId)")
//    Cursor loadAllByWordLabelAsCursor(Long wordId);

    @Update
    void update(Video video);

    @Query("DELETE FROM Video")
    void deleteAll();
}
