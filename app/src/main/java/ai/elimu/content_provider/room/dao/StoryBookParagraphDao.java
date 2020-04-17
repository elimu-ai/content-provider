package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.StoryBookParagraph;

@Dao
public interface StoryBookParagraphDao {

    @Insert
    void insert(StoryBookParagraph storyBookParagraph);

    @Query("SELECT * FROM StoryBookParagraph WHERE id = :id")
    StoryBookParagraph load(Long id);

    @Query("SELECT * FROM StoryBookParagraph WHERE id = :id")
    Cursor loadAsCursor(Long id);

    @Query("SELECT * FROM StoryBookParagraph WHERE storyBookChapterId = :storyBookChapterId")
    List<StoryBookParagraph> loadAll(Long storyBookChapterId);

    @Query("SELECT * FROM StoryBookParagraph WHERE storyBookChapterId = :storyBookChapterId")
    Cursor loadAllAsCursor(Long storyBookChapterId);

    @Update
    void update(StoryBookParagraph storyBookParagraph);
}
