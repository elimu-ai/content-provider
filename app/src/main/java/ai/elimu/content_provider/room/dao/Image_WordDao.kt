package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.Image_Word
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface Image_WordDao {
    @Insert
    fun insert(image_Word: Image_Word)

    @Update
    fun update(image_Word: Image_Word)

    @Query("DELETE FROM Image_Word WHERE Image_id = :imageId")
    fun delete(imageId: Long)

    @Query("DELETE FROM Image_Word")
    fun deleteAll()
}
