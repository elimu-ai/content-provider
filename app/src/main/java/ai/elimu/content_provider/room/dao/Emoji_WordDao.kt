package ai.elimu.content_provider.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ai.elimu.content_provider.room.entity.Emoji_Word;

@Dao
public interface Emoji_WordDao {

    @Insert
    void insert(Emoji_Word emoji_Word);

    @Update
    void update(Emoji_Word emoji_Word);

    @Query("DELETE FROM Emoji_Word WHERE Emoji_id = :emojiId")
    void delete(Long emojiId);

    @Query("DELETE FROM Emoji_Word")
    void deleteAll();
}
