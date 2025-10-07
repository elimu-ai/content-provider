package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.Word_LetterSound
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Word_LetterSoundDao {
    @Insert
    fun insert(word_letterSound: Word_LetterSound)

    @Query("DELETE FROM Word_LetterSound")
    fun deleteAll()
}
