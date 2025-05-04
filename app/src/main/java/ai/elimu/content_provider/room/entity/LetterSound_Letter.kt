package ai.elimu.content_provider.room.entity

import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
@Entity(primaryKeys = ["LetterSound_id", "letters_ORDER"])
class LetterSound_Letter {
    var LetterSound_id: Long = 0L

    @JvmField
    var letters_id: Long = 0L

    @JvmField
    var letters_ORDER: Int = 0
}
