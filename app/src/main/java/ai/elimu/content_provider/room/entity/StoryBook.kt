package ai.elimu.content_provider.room.entity

import ai.elimu.model.v2.enums.ReadingLevel
import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
@Entity
class StoryBook : Content() {
    @JvmField
    var title: String = ""

    @JvmField
    var description: String? = null

    @JvmField
    var coverImageId: Long = 0

    @JvmField
    var readingLevel: ReadingLevel? = null
}
