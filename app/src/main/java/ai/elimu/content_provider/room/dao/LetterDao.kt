package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.Letter
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LetterDao {
    @Insert
    fun insert(letter: Letter)

    @Query("SELECT * FROM Letter WHERE id = :id")
    fun load(id: Long?): Letter?

    @Query("SELECT * FROM Letter WHERE id = :id")
    fun loadCursor(id: Long?): Cursor

    @Query("SELECT * FROM Letter ORDER BY usageCount DESC")
    fun loadAllOrderedByUsageCount(): MutableList<Letter>

    @Query("SELECT * FROM Letter ORDER BY usageCount DESC")
    fun loadAllOrderedByUsageCountCursor(): Cursor

    @Query("SELECT * FROM Letter l " +
            "INNER JOIN LetterSound_Letter lsl ON l.id = lsl.letters_id " +
            "WHERE lsl.LetterSound_id = :letterSoundId " +
            "ORDER BY lsl.letters_ORDER")
    fun loadAllByLetterSound(letterSoundId: Long?): Cursor

    @Update
    fun update(letter: Letter)

    @Query("DELETE FROM Letter")
    fun deleteAll()
}
