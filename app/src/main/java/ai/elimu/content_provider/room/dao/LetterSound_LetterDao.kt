package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.LetterSound_Letter
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LetterSound_LetterDao {
    @Insert
    fun insert(letterSound_Letter: LetterSound_Letter)

    @Query("DELETE FROM LetterSound_Letter")
    fun deleteAll()
}
