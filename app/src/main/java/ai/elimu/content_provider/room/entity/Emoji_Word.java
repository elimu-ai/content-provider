package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * For documentation, see https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model
 */
@Entity(primaryKeys = {"Emoji_id", "words_id"})
public class Emoji_Word {

    @NonNull
    private Long Emoji_id;

    @NonNull
    private Long words_id;

    @NonNull
    public Long getEmoji_id() {
        return Emoji_id;
    }

    public void setEmoji_id(Long emoji_id) {
        Emoji_id = emoji_id;
    }

    public Long getWords_id() {
        return words_id;
    }

    public void setWords_id(Long words_id) {
        this.words_id = words_id;
    }
}
