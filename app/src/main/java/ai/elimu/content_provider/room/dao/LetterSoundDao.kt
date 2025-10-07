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
    fun loadCursor(id: Long?): Cursor

    @Query("SELECT * FROM LetterSound ORDER BY usageCount DESC")
    fun loadAll(): MutableList<LetterSound>

    @Query("SELECT * FROM LetterSound ORDER BY usageCount DESC")
    fun loadAllCursor(): Cursor

    @Query("SELECT * FROM LetterSound ls " +
            "INNER JOIN Word_LetterSound wls ON ls.id = wls.letterSounds_id " +
            "WHERE wls.Word_id = :wordId " +
            "ORDER BY wls.letterSounds_ORDER")
    fun loadAllByWord(wordId: Long): Cursor

    @Query("DELETE FROM LetterSound")
    fun deleteAll()
}
