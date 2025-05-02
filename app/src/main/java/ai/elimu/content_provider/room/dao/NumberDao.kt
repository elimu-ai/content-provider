package ai.elimu.content_provider.room.dao

import ai.elimu.content_provider.room.entity.Number
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NumberDao {
    @Insert
    fun insert(number: Number)

    @Query("SELECT * FROM Number WHERE id = :id")
    fun load(id: Long?): Number?

    @Query("SELECT * FROM Number WHERE id = :id")
    fun loadCursor(id: Long?): Cursor

    @Query("SELECT * FROM Number ORDER BY value")
    fun loadAllOrderedByValue(): MutableList<Number>

    @Query("SELECT * FROM Number ORDER BY value")
    fun loadAllOrderedByValueCursor(): Cursor

    @Update
    fun update(number: Number)

    @Query("DELETE FROM Number")
    fun deleteAll()
}
