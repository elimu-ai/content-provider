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

    @Query("SELECT * FROM StoryBook WHERE id = :id")
    StoryBook load(Long id);

    @Query("SELECT * FROM StoryBook WHERE id = :id")
    Cursor loadAsCursor(Long id);

    @Query("SELECT * FROM StoryBook ORDER BY readingLevel,title")
    List<StoryBook> loadAll();

    @Query("SELECT * FROM StoryBook ORDER BY readingLevel,title")
    Cursor loadAllAsCursor();

    @Update
    void update(StoryBook storyBook);

    @Query("DELETE FROM StoryBook")
    void deleteAll();
}
