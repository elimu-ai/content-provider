package ai.elimu.content_provider.room.entity

import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
@Entity(primaryKeys = ["StoryBookParagraph_id", "words_ORDER"])
class StoryBookParagraph_Word {
    var StoryBookParagraph_id: Long = 0L

    @JvmField
    var words_id: Long = 0L

    @JvmField
    var words_ORDER: Int = 0
}
