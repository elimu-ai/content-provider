package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.Sound;

@Dao
public interface SoundDao {

    @Insert
    void insert(Sound sound);

    @Query("SELECT * FROM Sound WHERE id = :id")
    Sound load(Long id);

    @Query("SELECT * FROM Sound WHERE id = :id")
    Cursor load_Cursor(Long id);

    @Query("SELECT * FROM Sound ORDER BY usageCount DESC")
    List<Sound> loadAllOrderedByUsageCount();

    @Query("SELECT * FROM Sound ORDER BY usageCount DESC")
    Cursor loadAllOrderedByUsageCount_Cursor();

    @Update
    void update(Sound sound);

    @Query("DELETE FROM Sound")
    void deleteAll();
}
