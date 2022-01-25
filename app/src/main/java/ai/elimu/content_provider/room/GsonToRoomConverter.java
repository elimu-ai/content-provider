package ai.elimu.content_provider.room;

import ai.elimu.content_provider.room.entity.Allophone;
import ai.elimu.content_provider.room.entity.Audio;
import ai.elimu.content_provider.room.entity.Emoji;
import ai.elimu.content_provider.room.entity.Image;
import ai.elimu.content_provider.room.entity.Letter;
import ai.elimu.content_provider.room.entity.Number;
import ai.elimu.content_provider.room.entity.StoryBook;
import ai.elimu.content_provider.room.entity.StoryBookChapter;
import ai.elimu.content_provider.room.entity.StoryBookParagraph;
import ai.elimu.content_provider.room.entity.Video;
import ai.elimu.content_provider.room.entity.Word;
import ai.elimu.model.v2.gson.content.SoundGson;
import ai.elimu.model.v2.gson.content.AudioGson;
import ai.elimu.model.v2.gson.content.EmojiGson;
import ai.elimu.model.v2.gson.content.ImageGson;
import ai.elimu.model.v2.gson.content.LetterGson;
import ai.elimu.model.v2.gson.content.NumberGson;
import ai.elimu.model.v2.gson.content.StoryBookChapterGson;
import ai.elimu.model.v2.gson.content.StoryBookGson;
import ai.elimu.model.v2.gson.content.StoryBookParagraphGson;
import ai.elimu.model.v2.gson.content.VideoGson;
import ai.elimu.model.v2.gson.content.WordGson;

public class GsonToRoomConverter {

    public static Letter getLetter(LetterGson letterGson) {
        if (letterGson == null) {
            return null;
        } else {
            Letter letter = new Letter();

            // BaseEntity
            letter.setId(letterGson.getId());

            // Content
            letter.setRevisionNumber(letterGson.getRevisionNumber());
            letter.setUsageCount(letterGson.getUsageCount());

            // Letter
            letter.setText(letterGson.getText());
            letter.setDiacritic(letterGson.getDiacritic());

            return letter;
        }
    }

    public static Allophone getAllophone(SoundGson allophoneGson) {
        if (allophoneGson == null) {
            return null;
        } else {
            Allophone allophone = new Allophone();

            // BaseEntity
            allophone.setId(allophoneGson.getId());

            // Content
            allophone.setRevisionNumber(allophoneGson.getRevisionNumber());
            allophone.setUsageCount(allophoneGson.getUsageCount());

            // Allophone
            allophone.setValueIpa(allophoneGson.getValueIpa());
            allophone.setDiacritic(allophoneGson.getDiacritic());

            return allophone;
        }
    }

    public static Word getWord(WordGson wordGson) {
        if (wordGson == null) {
            return null;
        } else {
            Word word = new Word();

            // BaseEntity
            word.setId(wordGson.getId());

            // Content
            word.setRevisionNumber(wordGson.getRevisionNumber());
            word.setUsageCount(wordGson.getUsageCount());

            // Word
            word.setText(wordGson.getText());
            word.setWordType(wordGson.getWordType());

            return word;
        }
    }

    public static Number getNumber(NumberGson numberGson) {
        if (numberGson == null) {
            return null;
        } else {
            Number number = new Number();

            // BaseEntity
            number.setId(numberGson.getId());

            // Content
            number.setRevisionNumber(numberGson.getRevisionNumber());
            number.setUsageCount(numberGson.getUsageCount());

            // Number
            number.setValue(numberGson.getValue());
            number.setSymbol(numberGson.getSymbol());

            return number;
        }
    }

    public static Emoji getEmoji(EmojiGson emojiGson) {
        if (emojiGson == null) {
            return null;
        } else {
            Emoji emoji = new Emoji();

            // BaseEntity
            emoji.setId(emojiGson.getId());

            // Content
            emoji.setRevisionNumber(emojiGson.getRevisionNumber());
            emoji.setUsageCount(emojiGson.getUsageCount());

            // Emoji
            emoji.setGlyph(emojiGson.getGlyph());
            emoji.setUnicodeVersion(emojiGson.getUnicodeVersion());
            emoji.setUnicodeEmojiVersion(emojiGson.getUnicodeEmojiVersion());

            return emoji;
        }
    }

    public static Image getImage(ImageGson imageGson) {
        if (imageGson == null) {
            return null;
        } else {
            Image image = new Image();

            // BaseEntity
            image.setId(imageGson.getId());

            // Content
            image.setRevisionNumber(imageGson.getRevisionNumber());
            image.setUsageCount(imageGson.getUsageCount());

            // Image
            image.setTitle(imageGson.getTitle());
            image.setImageFormat(imageGson.getImageFormat());

            return image;
        }
    }

    public static Audio getAudio(AudioGson audioGson) {
        if (audioGson == null) {
            return null;
        } else {
            Audio audio = new Audio();

            // BaseEntity
            audio.setId(audioGson.getId());

            // Content
            audio.setRevisionNumber(audioGson.getRevisionNumber());
            audio.setUsageCount(audioGson.getUsageCount());

            // Audio
            audio.setTitle(audioGson.getTitle());
            audio.setTranscription(audioGson.getTranscription());
            audio.setAudioFormat(audioGson.getAudioFormat());

            return audio;
        }
    }

    public static StoryBook getStoryBook(StoryBookGson storyBookGson) {
        if (storyBookGson == null) {
            return null;
        } else {
            StoryBook storyBook = new StoryBook();

            // BaseEntity
            storyBook.setId(storyBookGson.getId());

            // Content
            storyBook.setRevisionNumber(storyBookGson.getRevisionNumber());
            storyBook.setUsageCount(storyBookGson.getUsageCount());

            // StoryBook
            storyBook.setTitle(storyBookGson.getTitle());
            storyBook.setDescription(storyBookGson.getDescription());
            storyBook.setCoverImageId(storyBookGson.getCoverImage().getId());
            storyBook.setReadingLevel(storyBookGson.getReadingLevel());

            return storyBook;
        }
    }

    public static StoryBookChapter getStoryBookChapter(StoryBookChapterGson storyBookChapterGson) {
        if (storyBookChapterGson == null) {
            return null;
        } else {
            StoryBookChapter storyBookChapter = new StoryBookChapter();

            // BaseEntity
            storyBookChapter.setId(storyBookChapterGson.getId());

            // StoryBookChapter
//            storyBookChapter.setStoryBookId(storyBookChapterGson.getStoryBook().getId());
            storyBookChapter.setSortOrder(storyBookChapterGson.getSortOrder());
            if (storyBookChapterGson.getImage() != null) {
                storyBookChapter.setImageId(storyBookChapterGson.getImage().getId());
            }

            return storyBookChapter;
        }
    }

    public static StoryBookParagraph getStoryBookParagraph(StoryBookParagraphGson storyBookParagraphGson) {
        if (storyBookParagraphGson == null) {
            return null;
        } else {
            StoryBookParagraph storyBookParagraph = new StoryBookParagraph();

            // BaseEntity
            storyBookParagraph.setId(storyBookParagraphGson.getId());

            // StoryBookParagraph
//            storyBookParagraph.setStoryBookChapterId(storyBookParagraphGson.getStoryBookChapter().getId());
            storyBookParagraph.setSortOrder(storyBookParagraphGson.getSortOrder());
            storyBookParagraph.setOriginalText(storyBookParagraphGson.getOriginalText());
            // TODO: setWords

            return storyBookParagraph;
        }
    }

    public static Video getVideo(VideoGson videoGson) {
        if (videoGson == null) {
            return null;
        } else {
            Video video = new Video();

            // BaseEntity
            video.setId(videoGson.getId());

            // Content
            video.setRevisionNumber(videoGson.getRevisionNumber());
            video.setUsageCount(videoGson.getUsageCount());

            // Video
            video.setTitle(videoGson.getTitle());
            video.setVideoFormat(videoGson.getVideoFormat());

            return video;
        }
    }
}
