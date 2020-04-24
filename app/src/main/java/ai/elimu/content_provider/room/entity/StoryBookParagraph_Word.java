package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * For documentation, see https://github.com/elimu-ai/webapp/tree/master/src/main/java/ai/elimu/model
 */
@Entity(primaryKeys = {"StoryBookParagraph_id", "words_id"})
public class StoryBookParagraph_Word {

    @NonNull
    private Long StoryBookParagraph_id;

    @NonNull
    private Long words_id;

    @NonNull
    private Integer words_ORDER;

    public Long getStoryBookParagraph_id() {
        return StoryBookParagraph_id;
    }

    public void setStoryBookParagraph_id(Long storyBookParagraph_id) {
        StoryBookParagraph_id = storyBookParagraph_id;
    }

    public Long getWords_id() {
        return words_id;
    }

    public void setWords_id(Long words_id) {
        this.words_id = words_id;
    }

    public Integer getWords_ORDER() {
        return words_ORDER;
    }

    public void setWords_ORDER(Integer words_ORDER) {
        this.words_ORDER = words_ORDER;
    }
}
