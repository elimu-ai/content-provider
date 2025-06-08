package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.Sound
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SoundDao {
    @Insert
    fun insert(sound: Sound)

    @Query("SELECT * FROM Sound WHERE id = :id")
    fun load(id: Long?): Sound?

    @Query("SELECT * FROM Sound WHERE id = :id")
    fun loadCursor(id: Long?): Cursor

    @Query("SELECT * FROM Sound ORDER BY usageCount DESC")
    fun loadAllOrderedByUsageCount(): MutableList<Sound>

    @Query("SELECT * FROM Sound ORDER BY usageCount DESC")
    fun loadAllOrderedByUsageCountCursor(): Cursor

    @Query("SELECT * FROM Sound s " +
            "INNER JOIN LetterSound_Sound lss ON s.id = lss.sounds_id " +
            "WHERE lss.LetterSound_id = :letterSoundId " +
            "ORDER BY lss.sounds_ORDER")
    fun loadAllByLetterSound(letterSoundId: Long?): Cursor

    @Update
    fun update(sound: Sound)

    @Query("DELETE FROM Sound")
    fun deleteAll()
}
