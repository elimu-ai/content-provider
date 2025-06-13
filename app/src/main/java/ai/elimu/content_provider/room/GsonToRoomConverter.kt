package ai.elimu.content_provider.room

import ai.elimu.content_provider.room.entity.Emoji
import ai.elimu.content_provider.room.entity.Image
import ai.elimu.content_provider.room.entity.Letter
import ai.elimu.content_provider.room.entity.LetterSound
import ai.elimu.content_provider.room.entity.Number
import ai.elimu.content_provider.room.entity.Sound
import ai.elimu.content_provider.room.entity.StoryBook
import ai.elimu.content_provider.room.entity.StoryBookChapter
import ai.elimu.content_provider.room.entity.StoryBookParagraph
import ai.elimu.content_provider.room.entity.Video
import ai.elimu.content_provider.room.entity.Word
import ai.elimu.model.v2.gson.content.EmojiGson
import ai.elimu.model.v2.gson.content.ImageGson
import ai.elimu.model.v2.gson.content.LetterGson
import ai.elimu.model.v2.gson.content.LetterSoundGson
import ai.elimu.model.v2.gson.content.NumberGson
import ai.elimu.model.v2.gson.content.SoundGson
import ai.elimu.model.v2.gson.content.StoryBookChapterGson
import ai.elimu.model.v2.gson.content.StoryBookGson
import ai.elimu.model.v2.gson.content.StoryBookParagraphGson
import ai.elimu.model.v2.gson.content.VideoGson
import ai.elimu.model.v2.gson.content.WordGson

object GsonToRoomConverter {
    fun getLetter(letterGson: LetterGson): Letter {
        val letter = Letter()

        // BaseEntity
        letter.id = letterGson.id

        // Content
        letter.revisionNumber = letterGson.revisionNumber
        letter.usageCount = letterGson.usageCount

        // Letter
        letter.text = letterGson.text
        letter.diacritic = letterGson.diacritic

        return letter
    }

    @JvmStatic
    fun getSound(soundGson: SoundGson?): Sound? {
        if (soundGson == null) {
            return null
        } else {
            val sound = Sound()

            // BaseEntity
            sound.id = soundGson.id

            // Content
            sound.revisionNumber = soundGson.revisionNumber
            sound.usageCount = soundGson.usageCount

            // Sound
            sound.valueIpa = soundGson.valueIpa
            sound.diacritic = soundGson.diacritic

            return sound
        }
    }

    fun getLetterSound(letterSoundGson: LetterSoundGson): LetterSound {
        val letterSound = LetterSound()

        // BaseEntity
        letterSound.id = letterSoundGson.id

        // Content
        letterSound.revisionNumber = letterSoundGson.revisionNumber
        letterSound.usageCount = letterSoundGson.usageCount

        // Letter-sound correspondence
        // Note: letters are stored separately in LetterSound_Letter (see LetterSoundsFragment.java)
        // Note: sounds are stored separately in LetterSound_Sound (see LetterSoundsFragment.java)
        return letterSound
    }

    @JvmStatic
    fun getWord(wordGson: WordGson?): Word? {
        if (wordGson == null) {
            return null
        } else {
            val word = Word()

            // BaseEntity
            word.id = wordGson.id

            // Content
            word.revisionNumber = wordGson.revisionNumber
            word.usageCount = wordGson.usageCount

            // Word
            word.text = wordGson.text
            word.wordType = wordGson.wordType

            return word
        }
    }

    @JvmStatic
    fun getNumber(numberGson: NumberGson?): Number? {
        if (numberGson == null) {
            return null
        } else {
            val number = Number()

            // BaseEntity
            number.id = numberGson.id

            // Content
            number.revisionNumber = numberGson.revisionNumber
            number.usageCount = numberGson.usageCount

            // Number
            number.value = numberGson.value
            number.symbol = numberGson.symbol

            return number
        }
    }

    @JvmStatic
    fun getEmoji(emojiGson: EmojiGson?): Emoji? {
        if (emojiGson == null) {
            return null
        } else {
            val emoji = Emoji()

            // BaseEntity
            emoji.id = emojiGson.id

            // Content
            emoji.revisionNumber = emojiGson.revisionNumber
            emoji.usageCount = emojiGson.usageCount

            // Emoji
            emoji.glyph = emojiGson.glyph
            emoji.unicodeVersion = emojiGson.unicodeVersion
            emoji.unicodeEmojiVersion = emojiGson.unicodeEmojiVersion

            // Note: words are stored separately in Emoji_Word (see EmojisFragment.java)
            return emoji
        }
    }

    fun getImage(imageGson: ImageGson): Image {
        val image = Image()

        // BaseEntity
        image.id = imageGson.id

        // Content
        image.revisionNumber = imageGson.revisionNumber
        image.usageCount = imageGson.usageCount

        // Image
        image.title = imageGson.title
        image.imageFormat = imageGson.imageFormat
        image.checksumMd5 = imageGson.checksumMd5

        // Note: words are stored separately in Image_Word (see ImagesFragment.java)
        return image
    }

    @JvmStatic
    fun getStoryBook(storyBookGson: StoryBookGson?): StoryBook? {
        if (storyBookGson == null) {
            return null
        } else {
            val storyBook = StoryBook()

            // BaseEntity
            storyBook.id = storyBookGson.id

            // Content
            storyBook.revisionNumber = storyBookGson.revisionNumber
            storyBook.usageCount = storyBookGson.usageCount

            // StoryBook
            storyBook.title = storyBookGson.title
            storyBook.description = storyBookGson.description
            storyBook.coverImageId = storyBookGson.coverImage.id
            storyBook.readingLevel = storyBookGson.readingLevel

            return storyBook
        }
    }

    @JvmStatic
    fun getStoryBookChapter(storyBookChapterGson: StoryBookChapterGson?): StoryBookChapter? {
        if (storyBookChapterGson == null) {
            return null
        } else {
            val storyBookChapter = StoryBookChapter()

            // BaseEntity
            storyBookChapter.id = storyBookChapterGson.id

            // StoryBookChapter
//            storyBookChapter.setStoryBookId(storyBookChapterGson.getStoryBook().getId());
            storyBookChapter.sortOrder = storyBookChapterGson.sortOrder
            if (storyBookChapterGson.image != null) {
                storyBookChapter.imageId = storyBookChapterGson.image.id
            }

            return storyBookChapter
        }
    }

    @JvmStatic
    fun getStoryBookParagraph(storyBookParagraphGson: StoryBookParagraphGson?): StoryBookParagraph? {
        if (storyBookParagraphGson == null) {
            return null
        } else {
            val storyBookParagraph = StoryBookParagraph()

            // BaseEntity
            storyBookParagraph.id = storyBookParagraphGson.id

            // StoryBookParagraph
//            storyBookParagraph.setStoryBookChapterId(storyBookParagraphGson.getStoryBookChapter().getId());
            storyBookParagraph.sortOrder = storyBookParagraphGson.sortOrder
            storyBookParagraph.originalText = storyBookParagraphGson.originalText

            // Note: words are stored separately in StoryBookParagraph_Word (see StoryBooksFragment.java)
            return storyBookParagraph
        }
    }

    @JvmStatic
    fun getVideo(videoGson: VideoGson?): Video? {
        if (videoGson == null) {
            return null
        } else {
            val video = Video()

            // BaseEntity
            video.id = videoGson.id

            // Content
            video.revisionNumber = videoGson.revisionNumber
            video.usageCount = videoGson.usageCount

            // Video
            video.title = videoGson.title
            video.videoFormat = videoGson.videoFormat

            return video
        }
    }
}
