package ai.elimu.content_provider.room.entity;

import androidx.room.Entity;

/**
 * For documentation, see <a href="https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model">model</a>
 */
@Entity
public class Letter extends Content {

    private String text;

    private Boolean diacritic;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isDiacritic() {
        return diacritic;
    }

    public void setDiacritic(Boolean isDiacritic) {
        this.diacritic = isDiacritic;
    }
}
