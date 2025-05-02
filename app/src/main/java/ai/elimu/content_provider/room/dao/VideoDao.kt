package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.Video
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface VideoDao {
    @Insert
    fun insert(video: Video)

    @Query("SELECT * FROM Video WHERE id = :id")
    fun load(id: Long?): Video?

    @Query("SELECT * FROM Video WHERE id = :id")
    fun loadAsCursor(id: Long?): Cursor

    @Query("SELECT * FROM Video")
    fun loadAll(): MutableList<Video>

    @Query("SELECT * FROM Video")
    fun loadAllAsCursor(): Cursor

    @Query("SELECT * FROM Video WHERE title = :title")
    fun loadByTitleAsCursor(title: String?): Cursor

    @Update
    fun update(video: Video)

    @Query("DELETE FROM Video")
    fun deleteAll()
}
