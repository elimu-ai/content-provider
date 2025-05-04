package ai.elimu.content_provider.room.entity

import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
@Entity(primaryKeys = ["Image_id", "words_id"])
class Image_Word {
    var Image_id: Long = 0L

    @JvmField
    var words_id: Long = 0L
}
