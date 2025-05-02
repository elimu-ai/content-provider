package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.Word
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordDao {
    @Insert
    fun insert(word: Word)

    @Query("SELECT * FROM Word WHERE id = :id")
    fun load(id: Long?): Word?

    @Query("SELECT * FROM Word WHERE id = :id")
    fun loadAsCursor(id: Long?): Cursor

    @Query("SELECT * FROM Word ORDER BY usageCount DESC")
    fun loadAllOrderedByUsageCount(): MutableList<Word>

    @Query("SELECT * FROM Word ORDER BY usageCount DESC")
    fun loadAllOrderedByUsageCountAsCursor(): Cursor

    @Query("SELECT * FROM Word w WHERE w.id IN (SELECT words_id FROM StoryBookParagraph_Word WHERE StoryBookParagraph_id = :storyBookParagraphId)")
    fun loadAllAsCursor(storyBookParagraphId: Long?): Cursor

    @Update
    fun update(word: Word)

    @Query("DELETE FROM Word")
    fun deleteAll()
}
