package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import ai.elimu.model.v2.enums.ReadingLevel;

/**
 * For documentation, see https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model
 */
@Entity
public class StoryBook extends Content {

    @NonNull
    private String title;

    private String description;

    @NonNull
    private long coverImageId;

    private ReadingLevel readingLevel;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCoverImageId() {
        return coverImageId;
    }

    public void setCoverImageId(long coverImageId) {
        this.coverImageId = coverImageId;
    }

    public ReadingLevel getReadingLevel() {
        return readingLevel;
    }

    public void setReadingLevel(ReadingLevel readingLevel) {
        this.readingLevel = readingLevel;
    }
}
