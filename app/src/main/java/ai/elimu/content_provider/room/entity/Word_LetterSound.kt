package ai.elimu.content_provider.room.entity

import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/entity)
 */
@Entity(primaryKeys = ["Word_id", "letterSounds_ORDER"])
class Word_LetterSound {
    var Word_id: Long = 0L

    var letterSounds_id: Long = 0L

    var letterSounds_ORDER: Int = 0
}
