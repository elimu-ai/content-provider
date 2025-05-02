package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.StoryBook
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StoryBookDao {
    @Insert
    fun insert(storyBook: StoryBook)

    @Query("SELECT * FROM StoryBook WHERE id = :id")
    fun load(id: Long?): StoryBook?

    @Query("SELECT * FROM StoryBook WHERE id = :id")
    fun loadAsCursor(id: Long?): Cursor

    @Query("SELECT * FROM StoryBook ORDER BY readingLevel,title")
    fun loadAll(): MutableList<StoryBook>

    @Query("SELECT * FROM StoryBook ORDER BY readingLevel,title")
    fun loadAllAsCursor(): Cursor

    @Update
    fun update(storyBook: StoryBook)

    @Query("DELETE FROM StoryBook")
    fun deleteAll()
}
