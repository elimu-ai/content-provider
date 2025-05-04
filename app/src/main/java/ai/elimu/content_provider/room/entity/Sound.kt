package ai.elimu.content_provider.room.entity

import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
@Entity
class Sound : Content() {
    @JvmField
    var valueIpa: String? = null

    @JvmField
    var diacritic: Boolean? = null
}
