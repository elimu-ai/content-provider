package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.Emoji_Word
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EmojiWordDao {
    @Insert
    fun insert(emojiWord: Emoji_Word)

    @Update
    fun update(emojiWord: Emoji_Word)

    @Query("DELETE FROM Emoji_Word WHERE Emoji_id = :emojiId")
    fun delete(emojiId: Long?)

    @Query("DELETE FROM Emoji_Word")
    fun deleteAll()
}
