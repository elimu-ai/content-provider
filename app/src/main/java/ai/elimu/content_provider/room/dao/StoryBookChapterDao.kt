package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.StoryBookChapter
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StoryBookChapterDao {
    @Insert
    fun insert(storyBookChapter: StoryBookChapter)

    @Query("SELECT * FROM StoryBookChapter WHERE id = :id")
    fun load(id: Long?): StoryBookChapter?

    @Query("SELECT * FROM StoryBookChapter WHERE id = :id")
    fun loadAsCursor(id: Long?): Cursor

    @Query("SELECT * FROM StoryBookChapter WHERE storyBookId = :storyBookId")
    fun loadAll(storyBookId: Long?): MutableList<StoryBookChapter>

    @Query("SELECT * FROM StoryBookChapter WHERE storyBookId = :storyBookId")
    fun loadAllAsCursor(storyBookId: Long?): Cursor

    @Update
    fun update(storyBookChapter: StoryBookChapter)

    @Query("DELETE FROM StoryBookChapter")
    fun deleteAll()
}
