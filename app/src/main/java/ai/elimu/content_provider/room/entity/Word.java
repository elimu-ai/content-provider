package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import ai.elimu.model.enums.content.WordType;

/**
 * For documentation, see https://github.com/elimu-ai/webapp/tree/master/src/main/java/ai/elimu/model
 */
@Entity
public class Word extends Content {

    @NonNull
    private String text;

    private WordType wordType;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public WordType getWordType() {
        return wordType;
    }

    public void setWordType(WordType wordType) {
        this.wordType = wordType;
    }
}
