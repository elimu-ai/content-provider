package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import ai.elimu.model.v2.enums.content.ImageFormat;

/**
 * For documentation, see <a href="https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model">model</a>
 */
@Entity
public class Image extends Content {

    @NonNull
    private String title;

    @NonNull
    private ImageFormat imageFormat;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImageFormat getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(ImageFormat imageFormat) {
        this.imageFormat = imageFormat;
    }
}
