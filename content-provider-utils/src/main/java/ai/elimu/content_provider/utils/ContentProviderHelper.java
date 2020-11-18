package ai.elimu.content_provider.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ai.elimu.content_provider.utils.converter.CursorToEmojiGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToImageGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToLetterGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToStoryBookChapterGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToStoryBookGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToWordGsonConverter;
import ai.elimu.model.v2.gson.content.EmojiGson;
import ai.elimu.model.v2.gson.content.ImageGson;
import ai.elimu.model.v2.gson.content.LetterGson;
import ai.elimu.model.v2.gson.content.StoryBookChapterGson;
import ai.elimu.model.v2.gson.content.StoryBookGson;
import ai.elimu.model.v2.gson.content.StoryBookParagraphGson;
import ai.elimu.model.v2.gson.content.WordGson;

public class ContentProviderHelper {
    
    public static List<LetterGson> getLetterGsons(Context context, String contentProviderApplicationId) {
        List<LetterGson> letterGsons = new ArrayList<>();

        Uri uri = Uri.parse("content://" + contentProviderApplicationId + ".provider.letter_provider/letters");
        Log.i(ContentProviderHelper.class.getName(), "uri: " + uri);
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        Log.i(ContentProviderHelper.class.getName(), "cursor: " + cursor);
        if (cursor == null) {
            Log.e(ContentProviderHelper.class.getName(), "cursor == null");
            Toast.makeText(context, "cursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderHelper.class.getName(), "cursor.getCount(): " + cursor.getCount());
            if (cursor.getCount() == 0) {
                Log.e(ContentProviderHelper.class.getName(), "cursor.getCount() == 0");
            } else {
                boolean isLast = false;
                while (!isLast) {
                    cursor.moveToNext();

                    // Convert from Room to Gson
                    LetterGson letterGson = CursorToLetterGsonConverter.getLetterGson(cursor);

                    letterGsons.add(letterGson);

                    isLast = cursor.isLast();
                }
                cursor.close();
                Log.i(ContentProviderHelper.class.getName(), "cursor.isClosed(): " + cursor.isClosed());
            }
        }

        return letterGsons;
    }

    public static List<WordGson> getWordGsons(Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderHelper.class.getName(), "getWordGsons");

        List<WordGson> wordGsons = new ArrayList<>();
        
        Uri wordsUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.word_provider/words");
        Log.i(ContentProviderHelper.class.getName(), "wordsUri: " + wordsUri);
        Cursor wordsCursor = context.getContentResolver().query(wordsUri, null, null, null, null);
        Log.i(ContentProviderHelper.class.getName(), "wordsCursor: " + wordsCursor);
        if (wordsCursor == null) {
            Log.e(ContentProviderHelper.class.getName(), "wordsCursor == null");
            Toast.makeText(context, "wordsCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderHelper.class.getName(), "wordsCursor.getCount(): " + wordsCursor.getCount());
            if (wordsCursor.getCount() == 0) {
                Log.e(ContentProviderHelper.class.getName(), "wordsCursor.getCount() == 0");
            } else {
                boolean isLast = false;
                while (!isLast) {
                    wordsCursor.moveToNext();

                    // Convert from Room to Gson
                    WordGson wordGson = CursorToWordGsonConverter.getWordGson(wordsCursor);

                    wordGsons.add(wordGson);

                    isLast = wordsCursor.isLast();
                }

                wordsCursor.close();
                Log.i(ContentProviderHelper.class.getName(), "wordsCursor.isClosed(): " + wordsCursor.isClosed());
            }
        }
        Log.i(ContentProviderHelper.class.getName(), "wordGsons.size(): " + wordGsons.size());

        return wordGsons;
    }

    public static List<EmojiGson> getEmojiGsons(Long wordId, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderHelper.class.getName(), "getEmojiGsons");

        List<EmojiGson> emojiGsons = new ArrayList<>();
        
        Uri emojisUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.emoji_provider/emojis/by-word-label-id/" + wordId);
        Log.i(ContentProviderHelper.class.getName(), "emojisUri: " + emojisUri);
        Cursor emojisCursor = context.getContentResolver().query(emojisUri, null, null, null, null);
        Log.i(ContentProviderHelper.class.getName(), "emojisCursor: " + emojisCursor);
        if (emojisCursor == null) {
            Log.e(ContentProviderHelper.class.getName(), "emojisCursor == null");
            Toast.makeText(context, "emojisCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderHelper.class.getName(), "emojisCursor.getCount(): " + emojisCursor.getCount());
            if (emojisCursor.getCount() == 0) {
                Log.e(ContentProviderHelper.class.getName(), "emojisCursor.getCount() == 0");
            } else {
                boolean isLast = false;
                while (!isLast) {
                    emojisCursor.moveToNext();

                    // Convert from Room to Gson
                    EmojiGson emojiGson = CursorToEmojiGsonConverter.getEmojiGson(emojisCursor);

                    emojiGsons.add(emojiGson);

                    isLast = emojisCursor.isLast();
                }

                emojisCursor.close();
                Log.i(ContentProviderHelper.class.getName(), "emojisCursor.isClosed(): " + emojisCursor.isClosed());
            }
        }
        Log.i(ContentProviderHelper.class.getName(), "emojiGsons.size(): " + emojiGsons.size());

        return emojiGsons;
    }

    public static ImageGson getImageGson(Long imageId, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderHelper.class.getName(), "getImageGson");

        ImageGson imageGson = null;

        Uri imageUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.image_provider/images/" + imageId);
        Log.i(ContentProviderHelper.class.getName(), "imageUri: " + imageUri);
        Cursor imageCursor = context.getContentResolver().query(imageUri, null, null, null, null);
        Log.i(ContentProviderHelper.class.getName(), "imageCursor: " + imageCursor);
        if (imageCursor == null) {
            Log.e(ContentProviderHelper.class.getName(), "imageCursor == null");
            Toast.makeText(context, "imageCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderHelper.class.getName(), "imageCursor.getCount(): " + imageCursor.getCount());
            if (imageCursor.getCount() == 0) {
                Log.e(ContentProviderHelper.class.getName(), "imageCursor.getCount() == 0");
            } else {
                imageCursor.moveToFirst();

                // Convert from Room to Gson
                imageGson = CursorToImageGsonConverter.getImageGson(imageCursor);

                imageCursor.close();
                Log.i(ContentProviderHelper.class.getName(), "imageCursor.isClosed(): " + imageCursor.isClosed());
            }
        }
        Log.i(ContentProviderHelper.class.getName(), "imageGson: " + imageGson);

        return imageGson;
    }

    public static List<StoryBookGson> getStoryBookGsons(Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderHelper.class.getName(), "getStoryBookGsons");

        List<StoryBookGson> storyBookGsons = new ArrayList<>();

        Uri storyBooksUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.storybook_provider/storybooks");
        Log.i(ContentProviderHelper.class.getName(), "storyBooksUri: " + storyBooksUri);
        Cursor storyBooksCursor = context.getContentResolver().query(storyBooksUri, null, null, null, null);
        Log.i(ContentProviderHelper.class.getName(), "storyBooksCursor: " + storyBooksCursor);
        if (storyBooksCursor == null) {
            Log.e(ContentProviderHelper.class.getName(), "storyBooksCursor == null");
            Toast.makeText(context, "storyBooksCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderHelper.class.getName(), "storyBooksCursor.getCount(): " + storyBooksCursor.getCount());
            if (storyBooksCursor.getCount() == 0) {
                Log.e(ContentProviderHelper.class.getName(), "storyBooksCursor.getCount() == 0");
            } else {
                boolean isLast = false;
                while (!isLast) {
                    storyBooksCursor.moveToNext();

                    // Convert from Room to Gson
                    StoryBookGson storyBookGson = CursorToStoryBookGsonConverter.getStoryBookGson(storyBooksCursor);

                    storyBookGsons.add(storyBookGson);

                    isLast = storyBooksCursor.isLast();
                }

                storyBooksCursor.close();
                Log.i(ContentProviderHelper.class.getName(), "storyBooksCursor.isClosed(): " + storyBooksCursor.isClosed());
            }
        }
        Log.i(ContentProviderHelper.class.getName(), "storyBookGsons.size(): " + storyBookGsons.size());

        return storyBookGsons;
    }

    public static StoryBookGson getStoryBookGson(Long storyBookId, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderHelper.class.getName(), "getStoryBookGson");

        StoryBookGson storyBookGson = null;

        Uri storyBookUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.storybook_provider/storybooks/" + storyBookId);
        Log.i(ContentProviderHelper.class.getName(), "storyBookUri: " + storyBookUri);
        Cursor storyBookCursor = context.getContentResolver().query(storyBookUri, null, null, null, null);
        Log.i(ContentProviderHelper.class.getName(), "storyBookCursor: " + storyBookCursor);
        if (storyBookCursor == null) {
            Log.e(ContentProviderHelper.class.getName(), "storyBookCursor == null");
            Toast.makeText(context, "storyBookCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderHelper.class.getName(), "storyBookCursor.getCount(): " + storyBookCursor.getCount());
            if (storyBookCursor.getCount() == 0) {
                Log.e(ContentProviderHelper.class.getName(), "storyBookCursor.getCount() == 0");
            } else {
                storyBookCursor.moveToFirst();

                // Convert from Room to Gson
                storyBookGson = CursorToStoryBookGsonConverter.getStoryBookGson(storyBookCursor);

                storyBookCursor.close();
                Log.i(ContentProviderHelper.class.getName(), "storyBookCursor.isClosed(): " + storyBookCursor.isClosed());
            }
        }
        Log.i(ContentProviderHelper.class.getName(), "storyBookGson: " + storyBookGson);

        return storyBookGson;
    }

    public static List<StoryBookChapterGson> getStoryBookChapterGsons(Long storyBookId, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderHelper.class.getName(), "getStoryBookChapterGsons");

        List<StoryBookChapterGson> storyBookChapterGsons = new ArrayList<>();

        // Prepend cover image and book title as its own chapter
        StoryBookChapterGson coverChapterGson = new StoryBookChapterGson();
        StoryBookGson storyBookGson = getStoryBookGson(storyBookId, context, contentProviderApplicationId);
        if (storyBookGson.getCoverImage() != null) {
            ImageGson coverImageGson = getImageGson(storyBookGson.getCoverImage().getId(), context, contentProviderApplicationId);
            coverChapterGson.setImage(coverImageGson);
        }
        List<StoryBookParagraphGson> coverParagraphGsons = new ArrayList<>();
        StoryBookParagraphGson coverTitleParagraphGson = new StoryBookParagraphGson();
        coverTitleParagraphGson.setOriginalText(storyBookGson.getTitle());
        coverParagraphGsons.add(coverTitleParagraphGson);
        // TODO: add storybook description (if available)
        coverChapterGson.setStoryBookParagraphs(coverParagraphGsons);
        storyBookChapterGsons.add(coverChapterGson);

        Uri storyBookChaptersUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.storybook_provider/storybooks/" + storyBookId + "/chapters");
        Log.i(ContentProviderHelper.class.getName(), "storyBookChaptersUri: " + storyBookChaptersUri);
        Cursor storyBookChaptersCursor = context.getContentResolver().query(storyBookChaptersUri, null, null, null, null);
        Log.i(ContentProviderHelper.class.getName(), "storyBookChaptersCursor: " + storyBookChaptersCursor);
        if (storyBookChaptersCursor == null) {
            Log.e(ContentProviderHelper.class.getName(), "storyBookChaptersCursor == null");
            Toast.makeText(context, "storyBookChaptersCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderHelper.class.getName(), "storyBookChaptersCursor.getCount(): " + storyBookChaptersCursor.getCount());
            if (storyBookChaptersCursor.getCount() == 0) {
                Log.e(ContentProviderHelper.class.getName(), "storyBookChaptersCursor.getCount() == 0");
            } else {
                boolean isLast = false;
                while (!isLast) {
                    storyBookChaptersCursor.moveToNext();

                    // Convert from Room to Gson
                    StoryBookChapterGson storyBookChapterGson = CursorToStoryBookChapterGsonConverter.getStoryBookChapterGson(storyBookChaptersCursor, context, contentProviderApplicationId);

                    storyBookChapterGsons.add(storyBookChapterGson);

                    isLast = storyBookChaptersCursor.isLast();
                }

                storyBookChaptersCursor.close();
                Log.i(ContentProviderHelper.class.getName(), "storyBookChaptersCursor.isClosed(): " + storyBookChaptersCursor.isClosed());
            }
        }
        Log.i(ContentProviderHelper.class.getName(), "storyBookChapterGsons.size(): " + storyBookChapterGsons.size());

        return storyBookChapterGsons;
    }
}
