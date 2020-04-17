package ai.elimu.content_provider.room;

import ai.elimu.content_provider.room.entity.Image;
import ai.elimu.content_provider.room.entity.StoryBook;
import ai.elimu.content_provider.room.entity.StoryBookChapter;
import ai.elimu.content_provider.room.entity.StoryBookParagraph;
import ai.elimu.model.gson.v2.content.ImageGson;
import ai.elimu.model.gson.v2.content.StoryBookChapterGson;
import ai.elimu.model.gson.v2.content.StoryBookGson;
import ai.elimu.model.gson.v2.content.StoryBookParagraphGson;

public class GsonToRoomConverter {

    public static Image getImage(ImageGson imageGson) {
        if (imageGson == null) {
            return null;
        } else {
            Image image = new Image();

            // BaseEntity
            image.setId(imageGson.getId());

            // Content
            image.setRevisionNumber(imageGson.getRevisionNumber());

            // Image
            image.setTitle(imageGson.getTitle());
            image.setImageFormat(imageGson.getImageFormat());

            return image;
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

            // StoryBook
            storyBook.setTitle(storyBookGson.getTitle());
            storyBook.setDescription(storyBookGson.getDescription());
            storyBook.setCoverImageId(storyBookGson.getCoverImage().getId());

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
}
