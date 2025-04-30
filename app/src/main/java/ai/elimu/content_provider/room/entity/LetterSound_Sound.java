package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * For documentation, see <a href="https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model">model</a>
 */
@Entity(primaryKeys = {"LetterSound_id", "sounds_ORDER"})
public class LetterSound_Sound {

    @NonNull
    private Long LetterSound_id;

    @NonNull
    private Long sounds_id;

    @NonNull
    private Integer sounds_ORDER;

    public Long getLetterSound_id() {
        return LetterSound_id;
    }

    public void setLetterSound_id(Long letterSound_id) {
        LetterSound_id = letterSound_id;
    }

    public Long getSounds_id() {
        return sounds_id;
    }

    public void setSounds_id(Long sounds_id) {
        this.sounds_id = sounds_id;
    }

    public Integer getSounds_ORDER() {
        return sounds_ORDER;
    }

    public void setSounds_ORDER(Integer sounds_ORDER) {
        this.sounds_ORDER = sounds_ORDER;
    }
}
