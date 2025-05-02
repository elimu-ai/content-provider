package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.StoryBookChapter;

@Dao
public interface StoryBookChapterDao {

    @Insert
    void insert(StoryBookChapter storyBookChapter);

    @Query("SELECT * FROM StoryBookChapter WHERE id = :id")
    StoryBookChapter load(Long id);

    @Query("SELECT * FROM StoryBookChapter WHERE id = :id")
    Cursor loadAsCursor(Long id);

    @Query("SELECT * FROM StoryBookChapter WHERE storyBookId = :storyBookId")
    List<StoryBookChapter> loadAll(Long storyBookId);

    @Query("SELECT * FROM StoryBookChapter WHERE storyBookId = :storyBookId")
    Cursor loadAllAsCursor(Long storyBookId);

    @Update
    void update(StoryBookChapter storyBookChapter);

    @Query("DELETE FROM StoryBookChapter")
    void deleteAll();
}
