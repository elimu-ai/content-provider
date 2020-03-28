package ai.elimu.content_provider.room;

import ai.elimu.content_provider.room.entity.Image;
import ai.elimu.content_provider.room.entity.StoryBook;
import ai.elimu.model.gson.content.StoryBookGson;
import ai.elimu.model.gson.content.multimedia.ImageGson;

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

            return storyBook;
        }
    }
}
