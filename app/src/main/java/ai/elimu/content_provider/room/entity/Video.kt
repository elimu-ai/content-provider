package ai.elimu.content_provider.room.entity

import ai.elimu.model.v2.enums.content.VideoFormat
import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
@Entity
class Video : Content() {
    @JvmField
    var title: String = ""

    lateinit var videoFormat: VideoFormat
}
