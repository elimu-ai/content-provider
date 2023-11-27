package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * For documentation, see https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model
 */
@Entity
public class Emoji extends Content {

    @NonNull
    private String glyph;

    @NonNull
    private Double unicodeVersion;

    @NonNull
    private Double unicodeEmojiVersion;

    public String getGlyph() {
        return glyph;
    }

    public void setGlyph(String glyph) {
        this.glyph = glyph;
    }

    public Double getUnicodeVersion() {
        return unicodeVersion;
    }

    public void setUnicodeVersion(Double unicodeVersion) {
        this.unicodeVersion = unicodeVersion;
    }

    public Double getUnicodeEmojiVersion() {
        return unicodeEmojiVersion;
    }

    public void setUnicodeEmojiVersion(Double unicodeEmojiVersion) {
        this.unicodeEmojiVersion = unicodeEmojiVersion;
    }
}
