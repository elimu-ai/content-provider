package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.Emoji
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EmojiDao {
    @Insert
    fun insert(emoji: Emoji)

    @Query("SELECT * FROM Emoji WHERE id = :id")
    fun load(id: Long?): Emoji?

    @Query("SELECT * FROM Emoji WHERE id = :id")
    fun loadAsCursor(id: Long?): Cursor

    @Query("SELECT * FROM Emoji")
    fun loadAll(): MutableList<Emoji>

    @Query("SELECT * FROM Emoji")
    fun loadAllAsCursor(): Cursor

    @Query("SELECT * FROM Emoji e WHERE e.id IN (SELECT Emoji_id FROM Emoji_Word WHERE words_id = :wordId)")
    fun loadAllByWordLabelAsCursor(wordId: Long?): Cursor

    @Update
    fun update(emoji: Emoji)

    @Query("DELETE FROM Emoji")
    fun deleteAll()
}
