package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * For documentation, see https://github.com/elimu-ai/webapp/tree/master/src/main/java/ai/elimu/model
 */
@Entity(primaryKeys = {"Image_id", "words_id"})
public class Image_Word {

    @NonNull
    private Long Image_id;

    @NonNull
    private Long words_id;

    @NonNull
    public Long getImage_id() {
        return Image_id;
    }

    public void setImage_id(Long image_id) {
        Image_id = image_id;
    }

    public Long getWords_id() {
        return words_id;
    }

    public void setWords_id(Long words_id) {
        this.words_id = words_id;
    }
}
