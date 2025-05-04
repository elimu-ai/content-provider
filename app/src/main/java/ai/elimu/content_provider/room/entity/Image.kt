package ai.elimu.content_provider.room.entity

import ai.elimu.model.v2.enums.content.ImageFormat
import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
@Entity
class Image : Content() {
    lateinit var title: String

    lateinit var imageFormat: ImageFormat
}
