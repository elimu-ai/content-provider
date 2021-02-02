package ai.elimu.content_provider.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ai.elimu.analytics.utils.EventProviderUtil;
import ai.elimu.analytics.utils.logic.MasteryHelper;
import ai.elimu.content_provider.utils.converter.CursorToAudioGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToEmojiGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToImageGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToLetterGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToStoryBookChapterGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToStoryBookGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToWordGsonConverter;
import ai.elimu.model.v2.gson.analytics.LetterAssessmentEventGson;
import ai.elimu.model.v2.gson.analytics.WordAssessmentEventGson;
import ai.elimu.model.v2.gson.content.AudioGson;
import ai.elimu.model.v2.gson.content.EmojiGson;
import ai.elimu.model.v2.gson.content.ImageGson;
import ai.elimu.model.v2.gson.content.LetterGson;
import ai.elimu.model.v2.gson.content.StoryBookChapterGson;
import ai.elimu.model.v2.gson.content.StoryBookGson;
import ai.elimu.model.v2.gson.content.StoryBookParagraphGson;
import ai.elimu.model.v2.gson.content.WordGson;

public class ContentProviderUtil {

    /**
     * Returns a list of letters currently available to the student.
     * <p />
     * If the student has not yet mastered any letters, only the first letter will be returned.
     * <p />
     * If the student has already mastered one or more letters, those will be returned, plus one
     * additional letter to be mastered next.
     */
    public static List<LetterGson> getAvailableLetterGsons(Context context, String contentProviderApplicationId, String analyticsApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getAvailableLetterGsons");

        List<LetterGson> letterGsons = new ArrayList<>();

        List<LetterGson> allLetterGsons = getAllLetterGsons(context, contentProviderApplicationId);
        Log.i(ContentProviderUtil.class.getName(), "allLetterGsons.size(): " + allLetterGsons.size());
        for (LetterGson letterGson : allLetterGsons) {
            Log.i(ContentProviderUtil.class.getName(), "letterGson.getText(): \"" + letterGson.getText() + "\"");
            List<LetterAssessmentEventGson> letterAssessmentEventGsons = EventProviderUtil.getLetterAssessmentEventGsonsByLetter(letterGson, context, analyticsApplicationId);
            Log.i(ContentProviderUtil.class.getName(), "letterAssessmentEventGsons.size(): " + letterAssessmentEventGsons.size());
            boolean isLetterMastered = MasteryHelper.isLetterMastered(letterAssessmentEventGsons);
            Log.i(ContentProviderUtil.class.getName(), "isLetterMastered: " + isLetterMastered);
            letterGsons.add(letterGson);
            if (!isLetterMastered) {
                break;
            }
        }

        return letterGsons;
    }

    /**
     * This method is only meant to be used for testing purposes during development.
     */
    public static List<LetterGson> getAllLetterGsons(Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getAllLetterGsons");

        List<LetterGson> letterGsons = new ArrayList<>();

        Uri uri = Uri.parse("content://" + contentProviderApplicationId + ".provider.letter_provider/letters");
        Log.i(ContentProviderUtil.class.getName(), "uri: " + uri);
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        Log.i(ContentProviderUtil.class.getName(), "cursor: " + cursor);
        if (cursor == null) {
            Log.e(ContentProviderUtil.class.getName(), "cursor == null");
            Toast.makeText(context, "cursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderUtil.class.getName(), "cursor.getCount(): " + cursor.getCount());
            if (cursor.getCount() == 0) {
                Log.e(ContentProviderUtil.class.getName(), "cursor.getCount() == 0");
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
                Log.i(ContentProviderUtil.class.getName(), "cursor.isClosed(): " + cursor.isClosed());
            }
        }

        return letterGsons;
    }

    /**
     * Returns a list of words currently available to the student.
     * <p />
     * If the student has not yet mastered any words, only the first word will be returned.
     * <p />
     * If the student has already mastered one or more words, those will be returned, plus one
     * additional word to be mastered next.
     */
    public static List<WordGson> getAvailableWordGsons(Context context, String contentProviderApplicationId, String analyticsApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getAvailableWordGsons");

        List<WordGson> wordGsons = new ArrayList<>();

        List<WordGson> allWordGsons = getAllWordGsons(context, contentProviderApplicationId);
        Log.i(ContentProviderUtil.class.getName(), "allWordGsons.size(): " + allWordGsons.size());
        for (WordGson wordGson : allWordGsons) {
            Log.i(ContentProviderUtil.class.getName(), "wordGson.getText(): \"" + wordGson.getText() + "\"");
            List<WordAssessmentEventGson> wordAssessmentEventGsons = EventProviderUtil.getWordAssessmentEventGsonsByWord(wordGson, context, analyticsApplicationId);
            Log.i(ContentProviderUtil.class.getName(), "wordAssessmentEventGsons.size(): " + wordAssessmentEventGsons.size());
            boolean isWordMastered = MasteryHelper.isWordMastered(wordAssessmentEventGsons);
            Log.i(ContentProviderUtil.class.getName(), "isWordMastered: " + isWordMastered);
            wordGsons.add(wordGson);
            if (!isWordMastered) {
                break;
            }
        }

        return wordGsons;
    }

    public static List<WordGson> getAllWordGsons(Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getAllWordGsons");

        List<WordGson> wordGsons = new ArrayList<>();
        
        Uri wordsUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.word_provider/words");
        Log.i(ContentProviderUtil.class.getName(), "wordsUri: " + wordsUri);
        Cursor wordsCursor = context.getContentResolver().query(wordsUri, null, null, null, null);
        Log.i(ContentProviderUtil.class.getName(), "wordsCursor: " + wordsCursor);
        if (wordsCursor == null) {
            Log.e(ContentProviderUtil.class.getName(), "wordsCursor == null");
            Toast.makeText(context, "wordsCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderUtil.class.getName(), "wordsCursor.getCount(): " + wordsCursor.getCount());
            if (wordsCursor.getCount() == 0) {
                Log.e(ContentProviderUtil.class.getName(), "wordsCursor.getCount() == 0");
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
                Log.i(ContentProviderUtil.class.getName(), "wordsCursor.isClosed(): " + wordsCursor.isClosed());
            }
        }
        Log.i(ContentProviderUtil.class.getName(), "wordGsons.size(): " + wordGsons.size());

        return wordGsons;
    }

    public static WordGson getWordGson(Long wordId, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getWordGson");

        WordGson wordGson = null;

        Uri wordUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.word_provider/words/" + wordId);
        Log.i(ContentProviderUtil.class.getName(), "wordUri: " + wordUri);
        Cursor wordCursor = context.getContentResolver().query(wordUri, null, null, null, null);
        Log.i(ContentProviderUtil.class.getName(), "wordCursor: " + wordCursor);
        if (wordCursor == null) {
            Log.e(ContentProviderUtil.class.getName(), "wordCursor == null");
            Toast.makeText(context, "wordCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderUtil.class.getName(), "wordCursor.getCount(): " + wordCursor.getCount());
            if (wordCursor.getCount() == 0) {
                Log.e(ContentProviderUtil.class.getName(), "wordCursor.getCount() == 0");
            } else {
                wordCursor.moveToFirst();

                // Convert from Room to Gson
                wordGson = CursorToWordGsonConverter.getWordGson(wordCursor);

                wordCursor.close();
                Log.i(ContentProviderUtil.class.getName(), "wordCursor.isClosed(): " + wordCursor.isClosed());
            }
        }
        Log.i(ContentProviderUtil.class.getName(), "wordGson: " + wordGson);

        return wordGson;
    }

    public static List<EmojiGson> getAllEmojiGsons(Long wordId, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getAllEmojiGsons");

        List<EmojiGson> emojiGsons = new ArrayList<>();
        
        Uri emojisUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.emoji_provider/emojis/by-word-label-id/" + wordId);
        Log.i(ContentProviderUtil.class.getName(), "emojisUri: " + emojisUri);
        Cursor emojisCursor = context.getContentResolver().query(emojisUri, null, null, null, null);
        Log.i(ContentProviderUtil.class.getName(), "emojisCursor: " + emojisCursor);
        if (emojisCursor == null) {
            Log.e(ContentProviderUtil.class.getName(), "emojisCursor == null");
            Toast.makeText(context, "emojisCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderUtil.class.getName(), "emojisCursor.getCount(): " + emojisCursor.getCount());
            if (emojisCursor.getCount() == 0) {
                Log.e(ContentProviderUtil.class.getName(), "emojisCursor.getCount() == 0");
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
                Log.i(ContentProviderUtil.class.getName(), "emojisCursor.isClosed(): " + emojisCursor.isClosed());
            }
        }
        Log.i(ContentProviderUtil.class.getName(), "emojiGsons.size(): " + emojiGsons.size());

        return emojiGsons;
    }

    public static ImageGson getImageGson(Long imageId, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getImageGson");

        ImageGson imageGson = null;

        Uri imageUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.image_provider/images/" + imageId);
        Log.i(ContentProviderUtil.class.getName(), "imageUri: " + imageUri);
        Cursor imageCursor = context.getContentResolver().query(imageUri, null, null, null, null);
        Log.i(ContentProviderUtil.class.getName(), "imageCursor: " + imageCursor);
        if (imageCursor == null) {
            Log.e(ContentProviderUtil.class.getName(), "imageCursor == null");
            Toast.makeText(context, "imageCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderUtil.class.getName(), "imageCursor.getCount(): " + imageCursor.getCount());
            if (imageCursor.getCount() == 0) {
                Log.e(ContentProviderUtil.class.getName(), "imageCursor.getCount() == 0");
            } else {
                imageCursor.moveToFirst();

                // Convert from Room to Gson
                imageGson = CursorToImageGsonConverter.getImageGson(imageCursor);

                imageCursor.close();
                Log.i(ContentProviderUtil.class.getName(), "imageCursor.isClosed(): " + imageCursor.isClosed());
            }
        }
        Log.i(ContentProviderUtil.class.getName(), "imageGson: " + imageGson);

        return imageGson;
    }

    public static AudioGson getAudioGson(Long audioId, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getAudioGson");

        AudioGson audioGson = null;

        Uri audioUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.audio_provider/audios/" + audioId);
        Log.i(ContentProviderUtil.class.getName(), "audioUri: " + audioUri);
        Cursor audioCursor = context.getContentResolver().query(audioUri, null, null, null, null);
        Log.i(ContentProviderUtil.class.getName(), "audioCursor: " + audioCursor);
        if (audioCursor == null) {
            Log.e(ContentProviderUtil.class.getName(), "audioCursor == null");
            Toast.makeText(context, "audioCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderUtil.class.getName(), "audioCursor.getCount(): " + audioCursor.getCount());
            if (audioCursor.getCount() == 0) {
                Log.e(ContentProviderUtil.class.getName(), "audioCursor.getCount() == 0");
            } else {
                audioCursor.moveToFirst();

                // Convert from Room to Gson
                audioGson = CursorToAudioGsonConverter.getAudioGson(audioCursor);

                audioCursor.close();
                Log.i(ContentProviderUtil.class.getName(), "audioCursor.isClosed(): " + audioCursor.isClosed());
            }
        }
        Log.i(ContentProviderUtil.class.getName(), "audioGson: " + audioGson);

        return audioGson;
    }

    public static AudioGson getAudioGsonByTitle(String title, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getAudioGsonByTitle");

        AudioGson audioGson = null;

        Uri audioUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.audio_provider/audios/by-title/" + title);
        Log.i(ContentProviderUtil.class.getName(), "audioUri: " + audioUri);
        Cursor audioCursor = context.getContentResolver().query(audioUri, null, null, null, null);
        Log.i(ContentProviderUtil.class.getName(), "audioCursor: " + audioCursor);
        if (audioCursor == null) {
            Log.e(ContentProviderUtil.class.getName(), "audioCursor == null");
            Toast.makeText(context, "audioCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderUtil.class.getName(), "audioCursor.getCount(): " + audioCursor.getCount());
            if (audioCursor.getCount() == 0) {
                Log.e(ContentProviderUtil.class.getName(), "audioCursor.getCount() == 0");
            } else {
                audioCursor.moveToFirst();

                // Convert from Room to Gson
                audioGson = CursorToAudioGsonConverter.getAudioGson(audioCursor);

                audioCursor.close();
                Log.i(ContentProviderUtil.class.getName(), "audioCursor.isClosed(): " + audioCursor.isClosed());
            }
        }
        Log.i(ContentProviderUtil.class.getName(), "audioGson: " + audioGson);

        return audioGson;
    }

    public static AudioGson getAudioGsonByTranscription(String transcription, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getAudioGsonByTranscription");

        AudioGson audioGson = null;

        Uri audioUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.audio_provider/audios/by-transcription/" + transcription);
        Log.i(ContentProviderUtil.class.getName(), "audioUri: " + audioUri);
        Cursor audioCursor = context.getContentResolver().query(audioUri, null, null, null, null);
        Log.i(ContentProviderUtil.class.getName(), "audioCursor: " + audioCursor);
        if (audioCursor == null) {
            Log.e(ContentProviderUtil.class.getName(), "audioCursor == null");
            Toast.makeText(context, "audioCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderUtil.class.getName(), "audioCursor.getCount(): " + audioCursor.getCount());
            if (audioCursor.getCount() == 0) {
                Log.e(ContentProviderUtil.class.getName(), "audioCursor.getCount() == 0");
            } else {
                audioCursor.moveToFirst();

                // Convert from Room to Gson
                audioGson = CursorToAudioGsonConverter.getAudioGson(audioCursor);

                audioCursor.close();
                Log.i(ContentProviderUtil.class.getName(), "audioCursor.isClosed(): " + audioCursor.isClosed());
            }
        }
        Log.i(ContentProviderUtil.class.getName(), "audioGson: " + audioGson);

        return audioGson;
    }

    public static List<StoryBookGson> getAllStoryBookGsons(Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getAllStoryBookGsons");

        List<StoryBookGson> storyBookGsons = new ArrayList<>();

        Uri storyBooksUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.storybook_provider/storybooks");
        Log.i(ContentProviderUtil.class.getName(), "storyBooksUri: " + storyBooksUri);
        Cursor storyBooksCursor = context.getContentResolver().query(storyBooksUri, null, null, null, null);
        Log.i(ContentProviderUtil.class.getName(), "storyBooksCursor: " + storyBooksCursor);
        if (storyBooksCursor == null) {
            Log.e(ContentProviderUtil.class.getName(), "storyBooksCursor == null");
            Toast.makeText(context, "storyBooksCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderUtil.class.getName(), "storyBooksCursor.getCount(): " + storyBooksCursor.getCount());
            if (storyBooksCursor.getCount() == 0) {
                Log.e(ContentProviderUtil.class.getName(), "storyBooksCursor.getCount() == 0");
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
                Log.i(ContentProviderUtil.class.getName(), "storyBooksCursor.isClosed(): " + storyBooksCursor.isClosed());
            }
        }
        Log.i(ContentProviderUtil.class.getName(), "storyBookGsons.size(): " + storyBookGsons.size());

        return storyBookGsons;
    }

    public static StoryBookGson getStoryBookGson(Long storyBookId, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getStoryBookGson");

        StoryBookGson storyBookGson = null;

        Uri storyBookUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.storybook_provider/storybooks/" + storyBookId);
        Log.i(ContentProviderUtil.class.getName(), "storyBookUri: " + storyBookUri);
        Cursor storyBookCursor = context.getContentResolver().query(storyBookUri, null, null, null, null);
        Log.i(ContentProviderUtil.class.getName(), "storyBookCursor: " + storyBookCursor);
        if (storyBookCursor == null) {
            Log.e(ContentProviderUtil.class.getName(), "storyBookCursor == null");
            Toast.makeText(context, "storyBookCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderUtil.class.getName(), "storyBookCursor.getCount(): " + storyBookCursor.getCount());
            if (storyBookCursor.getCount() == 0) {
                Log.e(ContentProviderUtil.class.getName(), "storyBookCursor.getCount() == 0");
            } else {
                storyBookCursor.moveToFirst();

                // Convert from Room to Gson
                storyBookGson = CursorToStoryBookGsonConverter.getStoryBookGson(storyBookCursor);

                storyBookCursor.close();
                Log.i(ContentProviderUtil.class.getName(), "storyBookCursor.isClosed(): " + storyBookCursor.isClosed());
            }
        }
        Log.i(ContentProviderUtil.class.getName(), "storyBookGson: " + storyBookGson);

        return storyBookGson;
    }

    public static List<StoryBookChapterGson> getStoryBookChapterGsons(Long storyBookId, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderUtil.class.getName(), "getStoryBookChapterGsons");

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
        Log.i(ContentProviderUtil.class.getName(), "storyBookChaptersUri: " + storyBookChaptersUri);
        Cursor storyBookChaptersCursor = context.getContentResolver().query(storyBookChaptersUri, null, null, null, null);
        Log.i(ContentProviderUtil.class.getName(), "storyBookChaptersCursor: " + storyBookChaptersCursor);
        if (storyBookChaptersCursor == null) {
            Log.e(ContentProviderUtil.class.getName(), "storyBookChaptersCursor == null");
            Toast.makeText(context, "storyBookChaptersCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderUtil.class.getName(), "storyBookChaptersCursor.getCount(): " + storyBookChaptersCursor.getCount());
            if (storyBookChaptersCursor.getCount() == 0) {
                Log.e(ContentProviderUtil.class.getName(), "storyBookChaptersCursor.getCount() == 0");
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
                Log.i(ContentProviderUtil.class.getName(), "storyBookChaptersCursor.isClosed(): " + storyBookChaptersCursor.isClosed());
            }
        }
        Log.i(ContentProviderUtil.class.getName(), "storyBookChapterGsons.size(): " + storyBookChapterGsons.size());

        return storyBookChapterGsons;
    }
}
