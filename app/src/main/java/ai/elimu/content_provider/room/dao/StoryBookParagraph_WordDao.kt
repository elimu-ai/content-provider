package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.StoryBookParagraph_Word
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StoryBookParagraph_WordDao {
    @Insert
    fun insert(storyBookParagraph_Word: StoryBookParagraph_Word)

    @Update
    fun update(storyBookParagraph_Word: StoryBookParagraph_Word)

    @Query("DELETE FROM StoryBookParagraph_Word WHERE StoryBookParagraph_id = :storyBookParagraphId")
    fun delete(storyBookParagraphId: Long?)

    @Query("DELETE FROM StoryBookParagraph_Word")
    fun deleteAll()
}
