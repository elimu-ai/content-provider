package ai.elimu.content_provider.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ai.elimu.content_provider.room.entity.Image_Word;

@Dao
public interface Image_WordDao {

    @Insert
    void insert(Image_Word image_Word);

    @Update
    void update(Image_Word image_Word);

    @Query("DELETE FROM Image_Word WHERE Image_id = :imageId")
    void delete(Long imageId);
}
