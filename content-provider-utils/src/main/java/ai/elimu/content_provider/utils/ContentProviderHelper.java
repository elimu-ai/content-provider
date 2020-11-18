package ai.elimu.content_provider.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ai.elimu.content_provider.utils.converter.CursorToEmojiGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToLetterGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToWordAssessmentEventGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToWordGsonConverter;
import ai.elimu.content_provider.utils.converter.CursorToWordLearningEventGsonConverter;
import ai.elimu.model.v2.gson.analytics.WordAssessmentEventGson;
import ai.elimu.model.v2.gson.analytics.WordLearningEventGson;
import ai.elimu.model.v2.gson.content.EmojiGson;
import ai.elimu.model.v2.gson.content.LetterGson;
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

                    // Convert from Cursor to Gson
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

        // Fetch list of Words from the ContentProvider
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

    public static List<WordLearningEventGson> getWordLearningEventGsons(Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderHelper.class.getName(), "getWordLearningEventGsons");

        List<WordLearningEventGson> wordLearningEventGsons = new ArrayList<>();

        // Fetch list of WordLearningEvents from the Analytics application
        Uri wordLearningEventsUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.word_learning_event_provider/events");
        Log.i(ContentProviderHelper.class.getName(), "wordLearningEventsUri: " + wordLearningEventsUri);
        Cursor wordLearningEventsCursor = context.getContentResolver().query(wordLearningEventsUri, null, null, null, null);
        Log.i(ContentProviderHelper.class.getName(), "wordLearningEventsCursor: " + wordLearningEventsCursor);
        if (wordLearningEventsCursor == null) {
            Log.e(ContentProviderHelper.class.getName(), "wordLearningEventsCursor == null");
            Toast.makeText(context, "wordLearningEventsCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderHelper.class.getName(), "wordLearningEventsCursor.getCount(): " + wordLearningEventsCursor.getCount());
            if (wordLearningEventsCursor.getCount() == 0) {
                Log.e(ContentProviderHelper.class.getName(), "wordLearningEventsCursor.getCount() == 0");
            } else {
                boolean isLast = false;
                while (!isLast) {
                    wordLearningEventsCursor.moveToNext();

                    // Convert from Room to Gson
                    WordLearningEventGson wordLearningEventGson = CursorToWordLearningEventGsonConverter.getWordLearningEventGson(wordLearningEventsCursor);

                    wordLearningEventGsons.add(wordLearningEventGson);

                    isLast = wordLearningEventsCursor.isLast();
                }

                wordLearningEventsCursor.close();
                Log.i(ContentProviderHelper.class.getName(), "wordLearningEventsCursor.isClosed(): " + wordLearningEventsCursor.isClosed());
            }
        }
        Log.i(ContentProviderHelper.class.getName(), "wordLearningEventGsons.size(): " + wordLearningEventGsons.size());

        return wordLearningEventGsons;
    }

    public static Set<Long> getIdsOfWordsInWordLearningEvents(Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderHelper.class.getName(), "getIdsOfWordsInWordLearningEvents");

        Set<Long> wordIdsSet = new HashSet<>();

        // Fetch list of WordLearningEvents from the Analytics application
        Uri wordLearningEventsUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.word_learning_event_provider/events");
        Log.i(ContentProviderHelper.class.getName(), "wordLearningEventsUri: " + wordLearningEventsUri);
        Cursor wordLearningEventsCursor = context.getContentResolver().query(wordLearningEventsUri, null, null, null, null);
        Log.i(ContentProviderHelper.class.getName(), "wordLearningEventsCursor: " + wordLearningEventsCursor);
        if (wordLearningEventsCursor == null) {
            Log.e(ContentProviderHelper.class.getName(), "wordLearningEventsCursor == null");
            Toast.makeText(context, "wordLearningEventsCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderHelper.class.getName(), "wordLearningEventsCursor.getCount(): " + wordLearningEventsCursor.getCount());
            if (wordLearningEventsCursor.getCount() == 0) {
                Log.e(ContentProviderHelper.class.getName(), "wordLearningEventsCursor.getCount() == 0");
            } else {
                boolean isLast = false;
                while (!isLast) {
                    wordLearningEventsCursor.moveToNext();

                    // Convert from Room to Gson
                    WordLearningEventGson wordLearningEventGson = CursorToWordLearningEventGsonConverter.getWordLearningEventGson(wordLearningEventsCursor);

                    wordIdsSet.add(wordLearningEventGson.getWordId());

                    isLast = wordLearningEventsCursor.isLast();
                }

                wordLearningEventsCursor.close();
                Log.i(ContentProviderHelper.class.getName(), "wordLearningEventsCursor.isClosed(): " + wordLearningEventsCursor.isClosed());
            }
        }
        Log.i(ContentProviderHelper.class.getName(), "wordIdsSet.size(): " + wordIdsSet.size());

        return wordIdsSet;
    }

    public static List<WordAssessmentEventGson> getWordAssessmentEventGsons(Set<Long> idsOfWordsInWordLearningEvents, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderHelper.class.getName(), "getWordAssessmentEventGsons");

        List<WordAssessmentEventGson> wordAssessmentEventGsons = new ArrayList<>();

        // Fetch list of WordAssessmentEvents from the Analytics application
        Uri wordAssessmentEventsUri = Uri.parse("content://" + contentProviderApplicationId + ".provider.word_assessment_event_provider/events");
        Log.i(ContentProviderHelper.class.getName(), "wordAssessmentEventsUri: " + wordAssessmentEventsUri);
        Cursor wordAssessmentEventsCursor = context.getContentResolver().query(wordAssessmentEventsUri, null, null, null, null);
        Log.i(ContentProviderHelper.class.getName(), "wordAssessmentEventsCursor: " + wordAssessmentEventsCursor);
        if (wordAssessmentEventsCursor == null) {
            Log.e(ContentProviderHelper.class.getName(), "wordAssessmentEventsCursor == null");
            Toast.makeText(context, "wordAssessmentEventsCursor == null", Toast.LENGTH_LONG).show();
        } else {
            Log.i(ContentProviderHelper.class.getName(), "wordAssessmentEventsCursor.getCount(): " + wordAssessmentEventsCursor.getCount());
            if (wordAssessmentEventsCursor.getCount() == 0) {
                Log.e(ContentProviderHelper.class.getName(), "wordAssessmentEventsCursor.getCount() == 0");
            } else {
                boolean isLast = false;
                while (!isLast) {
                    wordAssessmentEventsCursor.moveToNext();

                    // Convert from Room to Gson
                    WordAssessmentEventGson wordAssessmentEventGson = CursorToWordAssessmentEventGsonConverter.getWordAssessmentEventGson(wordAssessmentEventsCursor);

                    wordAssessmentEventGsons.add(wordAssessmentEventGson);

                    isLast = wordAssessmentEventsCursor.isLast();
                }

                wordAssessmentEventsCursor.close();
                Log.i(ContentProviderHelper.class.getName(), "wordAssessmentEventsCursor.isClosed(): " + wordAssessmentEventsCursor.isClosed());
            }
        }
        Log.i(ContentProviderHelper.class.getName(), "wordAssessmentEventGsons.size(): " + wordAssessmentEventGsons.size());

        return wordAssessmentEventGsons;
    }

    public static List<EmojiGson> getEmojiGsons(Long wordId, Context context, String contentProviderApplicationId) {
        Log.i(ContentProviderHelper.class.getName(), "getEmojiGsons");

        List<EmojiGson> emojiGsons = new ArrayList<>();

        // Fetch list of WordAssessmentEvents from the Analytics application
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
}
