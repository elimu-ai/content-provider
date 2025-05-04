package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * For documentation, see <a href="https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model">model</a>
 */
@Entity
public class StoryBookParagraph extends BaseEntity {

    @NonNull
    private long storyBookChapterId;

    @NonNull
    private Integer sortOrder;

    @NonNull
    private String originalText;

    public long getStoryBookChapterId() {
        return storyBookChapterId;
    }

    public void setStoryBookChapterId(long storyBookChapterId) {
        this.storyBookChapterId = storyBookChapterId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }
}
