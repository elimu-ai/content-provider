package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.StoryBookParagraph
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StoryBookParagraphDao {
    @Insert
    fun insert(storyBookParagraph: StoryBookParagraph)

    @Query("SELECT * FROM StoryBookParagraph WHERE id = :id")
    fun load(id: Long?): StoryBookParagraph?

    @Query("SELECT * FROM StoryBookParagraph WHERE id = :id")
    fun loadAsCursor(id: Long?): Cursor?

    @Query("SELECT * FROM StoryBookParagraph WHERE storyBookChapterId = :storyBookChapterId")
    fun loadAll(storyBookChapterId: Long?): MutableList<StoryBookParagraph?>?

    @Query("SELECT * FROM StoryBookParagraph WHERE storyBookChapterId = :storyBookChapterId")
    fun loadAllAsCursor(storyBookChapterId: Long?): Cursor

    @Update
    fun update(storyBookParagraph: StoryBookParagraph)

    @Query("DELETE FROM StoryBookParagraph")
    fun deleteAll()
}
