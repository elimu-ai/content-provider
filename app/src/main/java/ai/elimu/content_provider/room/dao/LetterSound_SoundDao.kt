package ai.elimu.content_provider.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import ai.elimu.content_provider.room.entity.LetterSound_Sound;

@Dao
public interface LetterSound_SoundDao {

    @Insert
    void insert(LetterSound_Sound letterSound_Sound);

    @Query("DELETE FROM LetterSound_Sound")
    void deleteAll();
}
