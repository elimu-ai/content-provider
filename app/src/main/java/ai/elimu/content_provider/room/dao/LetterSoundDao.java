package ai.elimu.content_provider.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import ai.elimu.content_provider.room.entity.LetterSound;

@Dao
public interface LetterSoundDao {

    @Insert
    void insert(LetterSound letterSound);
}
