package ai.elimu.content_provider.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ai.elimu.content_provider.room.entity.Word;

@Dao
public interface WordDao {

    @Insert
    void insert(Word word);

    @Query("SELECT * FROM Word WHERE id = :id")
    Word load(Long id);

    @Query("SELECT * FROM Word WHERE id = :id")
    Cursor loadAsCursor(Long id);

    @Query("SELECT * FROM Word ORDER BY usageCount DESC")
    List<Word> loadAllOrderedByUsageCount();

    @Query("SELECT * FROM Word ORDER BY usageCount DESC")
    Cursor loadAllOrderedByUsageCountAsCursor();

    @Query("SELECT * FROM Word w WHERE w.id IN (SELECT words_id FROM StoryBookParagraph_Word WHERE StoryBookParagraph_id = :storyBookParagraphId)")
    Cursor loadAllAsCursor(Long storyBookParagraphId);

    @Update
    void update(Word word);
}
