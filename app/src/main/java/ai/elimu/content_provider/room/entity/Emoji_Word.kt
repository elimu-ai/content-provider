package ai.elimu.content_provider.room.entity

import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
@Entity(primaryKeys = ["Emoji_id", "words_id"])
class Emoji_Word {
    var Emoji_id: Long = 0L

    @JvmField
    var words_id: Long = 0L
}
