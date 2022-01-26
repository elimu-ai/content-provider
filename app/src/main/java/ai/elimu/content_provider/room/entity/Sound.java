package ai.elimu.content_provider.room.entity;

import androidx.room.Entity;

/**
 * For documentation, see https://github.com/elimu-ai/webapp/tree/master/src/main/java/ai/elimu/model
 */
@Entity
public class Sound extends Content {

    private String valueIpa;

    private Boolean diacritic;

    public String getValueIpa() {
        return valueIpa;
    }

    public void setValueIpa(String valueIpa) {
        this.valueIpa = valueIpa;
    }

    public Boolean getDiacritic() {
        return diacritic;
    }

    public void setDiacritic(Boolean diacritic) {
        this.diacritic = diacritic;
    }
}
