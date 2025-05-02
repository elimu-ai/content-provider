package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.Emoji;

@Dao
public interface EmojiDao {

    @Insert
    void insert(Emoji emoji);

    @Query("SELECT * FROM Emoji WHERE id = :id")
    Emoji load(Long id);

    @Query("SELECT * FROM Emoji WHERE id = :id")
    Cursor loadAsCursor(Long id);

    @Query("SELECT * FROM Emoji")
    List<Emoji> loadAll();

    @Query("SELECT * FROM Emoji")
    Cursor loadAllAsCursor();

    @Query("SELECT * FROM Emoji e WHERE e.id IN (SELECT Emoji_id FROM Emoji_Word WHERE words_id = :wordId)")
    Cursor loadAllByWordLabelAsCursor(Long wordId);

    @Update
    void update(Emoji emoji);

    @Query("DELETE FROM Emoji")
    void deleteAll();
}
