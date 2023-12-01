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

    @Query("SELECT * FROM Sound s WHERE s.id IN (SELECT sounds_id FROM LetterSound_Sound WHERE LetterSound_id = :letterSoundId)")
    Cursor loadAllByLetterSound(Long letterSoundId);

    @Update
    void update(Sound sound);

    @Query("DELETE FROM Sound")
    void deleteAll();
}
