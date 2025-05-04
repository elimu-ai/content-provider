package ai.elimu.content_provider.room.entity

import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
@Entity
class StoryBookParagraph : BaseEntity() {
    @JvmField
    var storyBookChapterId: Long = 0

    @JvmField
    var sortOrder: Int = 0

    @JvmField
    var originalText: String = ""
}
