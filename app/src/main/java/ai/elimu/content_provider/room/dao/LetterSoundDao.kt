package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.LetterSound
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LetterSoundDao {
    @Insert
    fun insert(letterSound: LetterSound)

    @Query("SELECT * FROM LetterSound WHERE id = :id")
    fun load_Cursor(id: Long?): Cursor

    @Query("SELECT * FROM LetterSound ORDER BY usageCount DESC")
    fun loadAll(): MutableList<LetterSound>

    @Query("SELECT * FROM LetterSound ORDER BY usageCount DESC")
    fun loadAll_Cursor(): Cursor

    @Query("DELETE FROM LetterSound")
    fun deleteAll()
}
