package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.Letter;

@Dao
public interface LetterDao {

    @Insert
    void insert(Letter letter);

    @Query("SELECT * FROM Letter WHERE id = :id")
    Letter load(Long id);

    @Query("SELECT * FROM Letter WHERE id = :id")
    Cursor loadAsCursor(Long id);

    @Query("SELECT * FROM Letter ORDER BY usageCount DESC")
    List<Letter> loadAllOrderedByUsageCount();

    @Query("SELECT * FROM Letter ORDER BY usageCount DESC")
    Cursor loadAllOrderedByUsageCount_Cursor();

    @Update
    void update(Letter letter);
}
