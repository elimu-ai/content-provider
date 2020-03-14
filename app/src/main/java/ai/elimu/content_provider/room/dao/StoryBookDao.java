package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ai.elimu.content_provider.room.entity.StoryBook;

@Dao
public interface StoryBookDao {

    @Insert
    void insert(StoryBook storyBook);

    @Query("SELECT * FROM StoryBook")
    List<StoryBook> loadAll();

    @Query("SELECT * FROM StoryBook")
    Cursor loadAllAsCursor();
}
