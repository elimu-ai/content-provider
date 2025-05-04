package ai.elimu.content_provider.room.entity

import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
@Entity
class Number : Content() {
    @JvmField
    var value: Int = 0

    @JvmField
    var symbol: String? = null
}
