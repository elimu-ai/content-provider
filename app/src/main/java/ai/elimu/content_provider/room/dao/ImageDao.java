package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.Image;

@Dao
public interface ImageDao {

    @Insert
    void insert(Image image);

    @Query("SELECT * FROM Image WHERE id = :id")
    Image load(Long id);

    @Query("SELECT * FROM Image WHERE id = :id")
    Cursor loadAsCursor(Long id);

    @Query("SELECT * FROM Image")
    List<Image> loadAll();

    @Query("SELECT * FROM Image")
    Cursor loadAllAsCursor();

    @Query("SELECT * FROM Image i WHERE i.id IN (SELECT Image_id FROM Image_Word WHERE words_id = :wordId)")
    Cursor loadAllByWordLabelAsCursor(Long wordId);

    @Update
    void update(Image image);
}
