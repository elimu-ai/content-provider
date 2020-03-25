package ai.elimu.content_provider.room;

import ai.elimu.content_provider.room.entity.StoryBook;
import ai.elimu.model.gson.content.StoryBookGson;

public class GsonToRoomConverter {

    public static StoryBook getStoryBook(StoryBookGson storyBookGson) {
        if (storyBookGson == null) {
            return null;
        } else {
            StoryBook storyBook = new StoryBook();

            storyBook.setId(storyBookGson.getId());

            storyBook.setTitle(storyBookGson.getTitle());
            storyBook.setDescription(storyBookGson.getDescription());

            return storyBook;
        }
    }
}
