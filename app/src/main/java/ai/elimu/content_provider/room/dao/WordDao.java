package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.Word;

@Dao
public interface WordDao {

    @Insert
    void insert(Word word);

    @Query("SELECT * FROM Word WHERE id = :id")
    Word load(Long id);

    @Query("SELECT * FROM Word WHERE id = :id")
    Cursor loadAsCursor(Long id);

    @Query("SELECT * FROM Word")
    List<Word> loadAll();

    @Query("SELECT * FROM Word")
    Cursor loadAllAsCursor();

    @Update
    void update(Word word);
}
