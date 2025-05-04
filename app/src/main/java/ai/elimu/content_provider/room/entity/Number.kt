package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * For documentation, see <a href="https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model">model</a>
 */
@Entity
public class Number extends Content {

    @NonNull
    private Integer value;

    private String symbol;


    public Integer getValue() {
        return value;
    }

    public void setValue(@NonNull Integer value) {
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
