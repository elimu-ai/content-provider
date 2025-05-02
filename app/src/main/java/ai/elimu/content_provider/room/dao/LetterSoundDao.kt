package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ai.elimu.content_provider.room.entity.LetterSound;

@Dao
public interface LetterSoundDao {

    @Insert
    void insert(LetterSound letterSound);

    @Query("SELECT * FROM LetterSound WHERE id = :id")
    Cursor load_Cursor(Long id);

    @Query("SELECT * FROM LetterSound ORDER BY usageCount DESC")
    List<LetterSound> loadAll();

    @Query("SELECT * FROM LetterSound ORDER BY usageCount DESC")
    Cursor loadAll_Cursor();

    @Query("DELETE FROM LetterSound")
    void deleteAll();
}
