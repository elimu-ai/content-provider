package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * For documentation, see <a href="https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model">model</a>
 */
@Entity
public class StoryBookChapter extends BaseEntity {

    @NonNull
    private long storyBookId;

    @NonNull
    private Integer sortOrder;

    @NonNull
    private long imageId;

    public long getStoryBookId() {
        return storyBookId;
    }

    public void setStoryBookId(long storyBookId) {
        this.storyBookId = storyBookId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
}
