package ai.elimu.content_provider.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import ai.elimu.content_provider.room.entity.LetterSound_Letter;

@Dao
public interface LetterSound_LetterDao {

    @Insert
    void insert(LetterSound_Letter letterSound_Letter);

    @Query("DELETE FROM LetterSound_Letter")
    void deleteAll();
}
