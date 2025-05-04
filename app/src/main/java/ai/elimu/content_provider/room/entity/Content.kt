package ai.elimu.content_provider.room.entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
open class Content : BaseEntity() {
    @JvmField
    var revisionNumber: Int = 0

    @JvmField
    var usageCount: Int? = null
}
