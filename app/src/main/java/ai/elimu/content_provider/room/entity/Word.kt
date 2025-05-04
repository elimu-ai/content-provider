package ai.elimu.content_provider.room.entity

import ai.elimu.model.v2.enums.content.WordType
import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
@Entity
class Word : Content() {
    @JvmField
    var text: String = ""

    @JvmField
    var wordType: WordType? = null
}
