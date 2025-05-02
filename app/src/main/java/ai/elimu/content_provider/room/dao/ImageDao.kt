package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.Image
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ImageDao {
    @Insert
    fun insert(image: Image)

    @Query("SELECT * FROM Image WHERE id = :id")
    fun load(id: Long?): Image?

    @Query("SELECT * FROM Image WHERE id = :id")
    fun loadAsCursor(id: Long?): Cursor

    @Query("SELECT * FROM Image")
    fun loadAll(): MutableList<Image>

    @Query("SELECT * FROM Image")
    fun loadAllAsCursor(): Cursor

    @Query("SELECT * FROM Image i WHERE i.id IN (SELECT Image_id FROM Image_Word WHERE words_id = :wordId)")
    fun loadAllByWordLabelAsCursor(wordId: Long?): Cursor

    @Update
    fun update(image: Image)

    @Query("DELETE FROM Image")
    fun deleteAll()
}
