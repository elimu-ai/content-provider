package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.Allophone;

@Dao
public interface AllophoneDao {

    @Insert
    void insert(Allophone allophone);

    @Query("SELECT * FROM Allophone WHERE id = :id")
    Allophone load(Long id);

    @Query("SELECT * FROM Allophone WHERE id = :id")
    Cursor load_Cursor(Long id);

    @Query("SELECT * FROM Allophone ORDER BY usageCount DESC")
    List<Allophone> loadAllOrderedByUsageCount();

    @Query("SELECT * FROM Allophone ORDER BY usageCount DESC")
    Cursor loadAllOrderedByUsageCount_Cursor();

    @Update
    void update(Allophone allophone);

    @Query("DELETE FROM Allophone")
    void deleteAll();
}
