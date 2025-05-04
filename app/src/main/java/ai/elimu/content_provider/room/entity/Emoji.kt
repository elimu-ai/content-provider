package ai.elimu.content_provider.room.entity

import androidx.room.Entity

/**
 * For documentation, see [model](https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model)
 */
@Entity
class Emoji : Content() {
    var glyph: String = ""

    var unicodeVersion: Double = 0.0

    var unicodeEmojiVersion: Double = 0.0
}
