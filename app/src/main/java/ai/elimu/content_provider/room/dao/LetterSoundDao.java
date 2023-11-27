package ai.elimu.content_provider.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ai.elimu.content_provider.room.entity.LetterSound;

@Dao
public interface LetterSoundDao {

    @Insert
    void insert(LetterSound letterSound);

    @Query("SELECT * FROM LetterSound")
    List<LetterSound> loadAll();

    @Query("DELETE FROM LetterSound")
    void deleteAll();
}
