package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * For documentation, see https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model
 */
@Entity(primaryKeys = {"LetterSound_id", "letters_ORDER"})
public class LetterSound_Letter {

    @NonNull
    private Long LetterSound_id;

    @NonNull
    private Long letters_id;

    @NonNull
    private Integer letters_ORDER;

    public Long getLetterSound_id() {
        return LetterSound_id;
    }

    public void setLetterSound_id(Long letterSound_id) {
        LetterSound_id = letterSound_id;
    }

    public Long getLetters_id() {
        return letters_id;
    }

    public void setLetters_id(Long letters_id) {
        this.letters_id = letters_id;
    }

    public Integer getLetters_ORDER() {
        return letters_ORDER;
    }

    public void setLetters_ORDER(Integer letters_ORDER) {
        this.letters_ORDER = letters_ORDER;
    }
}
