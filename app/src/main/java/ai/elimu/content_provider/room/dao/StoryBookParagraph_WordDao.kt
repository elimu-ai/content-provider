package ai.elimu.content_provider.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ai.elimu.content_provider.room.entity.StoryBookParagraph_Word;

@Dao
public interface StoryBookParagraph_WordDao {

    @Insert
    void insert(StoryBookParagraph_Word storyBookParagraph_Word);

    @Update
    void update(StoryBookParagraph_Word storyBookParagraph_Word);

    @Query("DELETE FROM StoryBookParagraph_Word WHERE StoryBookParagraph_id = :storyBookParagraphId")
    void delete(Long storyBookParagraphId);

    @Query("DELETE FROM StoryBookParagraph_Word")
    void deleteAll();
}
