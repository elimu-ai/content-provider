package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.Number;

@Dao
public interface NumberDao {

    @Insert
    void insert(Number number);

    @Query("SELECT * FROM Number WHERE id = :id")
    Number load(Long id);

    @Query("SELECT * FROM Number WHERE id = :id")
    Cursor load_Cursor(Long id);

    @Query("SELECT * FROM Number ORDER BY value")
    List<Number> loadAllOrderedByValue();

    @Query("SELECT * FROM Number ORDER BY value")
    Cursor loadAllOrderedByValue_Cursor();

    @Update
    void update(Number number);

    @Query("DELETE FROM Number")
    void deleteAll();
}
