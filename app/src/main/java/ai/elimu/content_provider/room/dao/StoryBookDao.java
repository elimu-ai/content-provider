package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.StoryBook;

@Dao
public interface StoryBookDao {

    @Insert
    void insert(StoryBook storyBook);

    @Query("SELECT * FROM StoryBook sb WHERE sb.id = :id")
    StoryBook load(Long id);

    @Query("SELECT * FROM StoryBook sb WHERE sb.id = :id")
    Cursor loadAsCursor(Long id);

    @Query("SELECT * FROM StoryBook")
    List<StoryBook> loadAll();

    @Query("SELECT * FROM StoryBook")
    Cursor loadAllAsCursor();

    @Update
    void update(StoryBook storyBook);
}
