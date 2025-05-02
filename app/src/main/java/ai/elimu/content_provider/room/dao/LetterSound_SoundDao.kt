package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.LetterSound_Sound
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LetterSound_SoundDao {
    @Insert
    fun insert(letterSound_Sound: LetterSound_Sound)

    @Query("DELETE FROM LetterSound_Sound")
    fun deleteAll()
}
