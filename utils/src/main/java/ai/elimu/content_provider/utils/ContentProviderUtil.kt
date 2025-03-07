package ai.elimu.content_provider.utils

import ai.elimu.analytics.utils.EventProviderUtil
import ai.elimu.analytics.utils.logic.MasteryHelper
import ai.elimu.content_provider.utils.converter.CursorToEmojiGsonConverter
import ai.elimu.content_provider.utils.converter.CursorToImageGsonConverter
import ai.elimu.content_provider.utils.converter.CursorToLetterGsonConverter
import ai.elimu.content_provider.utils.converter.CursorToLetterSoundGsonConverter
import ai.elimu.content_provider.utils.converter.CursorToStoryBookChapterGsonConverter
import ai.elimu.content_provider.utils.converter.CursorToStoryBookGsonConverter
import ai.elimu.content_provider.utils.converter.CursorToWordGsonConverter
import ai.elimu.model.v2.gson.content.EmojiGson
import ai.elimu.model.v2.gson.content.ImageGson
import ai.elimu.model.v2.gson.content.LetterGson
import ai.elimu.model.v2.gson.content.LetterSoundGson
import ai.elimu.model.v2.gson.content.StoryBookChapterGson
import ai.elimu.model.v2.gson.content.StoryBookGson
import ai.elimu.model.v2.gson.content.StoryBookParagraphGson
import ai.elimu.model.v2.gson.content.WordGson
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast

object ContentProviderUtil {

    private const val TAG = "ContentProviderUtil"

    /**
     * Returns a list of letters currently available to the student.
     *
     *
     * If the student has not yet mastered any letters, only the first letter will be returned.
     *
     *
     * If the student has already mastered one or more letters, those will be returned, plus one
     * additional letter to be mastered next.
     */
    @Deprecated("")
    fun getAvailableLetterGsons(
        context: Context,
        contentProviderApplicationId: String,
        analyticsApplicationId: String?
    ): List<LetterGson> {
        Log.i(TAG, "getAvailableLetterGsons")

        val letterGsons: MutableList<LetterGson> = ArrayList()

        val allLetterGsons = getAllLetterGsons(context, contentProviderApplicationId)
        Log.i(TAG, "allLetterGsons.size(): " + allLetterGsons.size)
        for (letterGson in allLetterGsons) {
            Log.i(
                TAG,
                "letterGson.getText(): \"" + letterGson.text + "\""
            )
            val letterAssessmentEventGsons =
                EventProviderUtil.getLetterAssessmentEventGsonsByLetter(
                    letterGson,
                    context,
                    analyticsApplicationId
                )
            Log.i(
                TAG,
                "letterAssessmentEventGsons.size(): " + letterAssessmentEventGsons.size
            )
            val isLetterMastered = MasteryHelper.isLetterMastered(letterAssessmentEventGsons)
            Log.i(
                TAG,
                "isLetterMastered: $isLetterMastered"
            )
            letterGsons.add(letterGson)
            if (!isLetterMastered) {
                break
            }
        }

        return letterGsons
    }

    /**
     * This method is only meant to be used for testing purposes during development. For production,
     * use [.getAvailableLetterGsons] instead.
     */
    @Deprecated("")
    fun getAllLetterGsons(
        context: Context,
        contentProviderApplicationId: String
    ): List<LetterGson> {
        Log.i(TAG, "getAllLetterGsons")

        val letterGsons: MutableList<LetterGson> = ArrayList()

        val uri =
            Uri.parse("content://$contentProviderApplicationId.provider.letter_provider/letters")
        Log.i(TAG, "uri: $uri")
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        Log.i(TAG, "cursor: $cursor")
        if (cursor == null) {
            Log.e(TAG, "cursor == null")
            Toast.makeText(context, "cursor == null", Toast.LENGTH_LONG).show()
        } else {
            Log.i(TAG, "cursor.getCount(): " + cursor.count)
            if (cursor.count == 0) {
                Log.e(TAG, "cursor.getCount() == 0")
            } else {
                var isLast = false
                while (!isLast) {
                    cursor.moveToNext()

                    // Convert from Room to Gson
                    val letterGson = CursorToLetterGsonConverter.getLetterGson(cursor)

                    letterGsons.add(letterGson)

                    isLast = cursor.isLast
                }
                cursor.close()
                Log.i(TAG, "cursor.isClosed(): " + cursor.isClosed)
            }
        }

        return letterGsons
    }

    /**
     * This method is only meant to be used for testing purposes during development. For production,
     * use [.getAvailableLetterSoundGsons] instead.
     */
    fun getAllLetterSoundGsons(
        context: Context,
        contentProviderApplicationId: String
    ): List<LetterSoundGson> {
        Log.i(TAG, "getAllLetterSoundGsons")

        val letterSoundGsons: MutableList<LetterSoundGson> = ArrayList()

        val uri =
            Uri.parse("content://$contentProviderApplicationId.provider.letter_sound_provider/letter_sounds")
        Log.i(TAG, "uri: $uri")
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        Log.i(TAG, "cursor: $cursor")
        if (cursor == null) {
            Log.e(TAG, "cursor == null")
            Toast.makeText(context, "cursor == null", Toast.LENGTH_LONG).show()
        } else {
            Log.i(TAG, "cursor.getCount(): " + cursor.count)
            if (cursor.count == 0) {
                Log.e(TAG, "cursor.getCount() == 0")
            } else {
                var isLast = false
                while (!isLast) {
                    cursor.moveToNext()

                    // Convert from Room to Gson
                    val letterSoundGson = CursorToLetterSoundGsonConverter.getLetterSoundGson(
                        cursor,
                        context,
                        contentProviderApplicationId
                    )

                    letterSoundGsons.add(letterSoundGson)

                    isLast = cursor.isLast
                }
                cursor.close()
                Log.i(TAG, "cursor.isClosed(): " + cursor.isClosed)
            }
        }

        return letterSoundGsons
    }

    /**
     * Returns a list of words currently available to the student.
     *
     *
     * If the student has not yet mastered any words, only the first word will be returned.
     *
     *
     * If the student has already mastered one or more words, those will be returned, plus one
     * additional word to be mastered next.
     */
    fun getAvailableWordGsons(
        context: Context,
        contentProviderApplicationId: String,
        analyticsApplicationId: String?
    ): List<WordGson> {
        Log.i(TAG, "getAvailableWordGsons")

        val wordGsons: MutableList<WordGson> = ArrayList()

        val allWordGsons = getAllWordGsons(context, contentProviderApplicationId)
        Log.i(TAG, "allWordGsons.size(): " + allWordGsons.size)
        for (wordGson in allWordGsons) {
            Log.i(
                TAG,
                "wordGson.getText(): \"" + wordGson.text + "\""
            )
            val wordAssessmentEventGsons = EventProviderUtil.getWordAssessmentEventGsonsByWord(
                wordGson,
                context,
                analyticsApplicationId
            )
            Log.i(
                TAG,
                "wordAssessmentEventGsons.size(): " + wordAssessmentEventGsons.size
            )
            val isWordMastered = MasteryHelper.isWordMastered(wordAssessmentEventGsons)
            Log.i(
                TAG,
                "isWordMastered: $isWordMastered"
            )
            wordGsons.add(wordGson)
            if (!isWordMastered) {
                break
            }
        }

        return wordGsons
    }

    /**
     * This method is only meant to be used for testing purposes during development. For production,
     * use [.getAvailableWordGsons] instead.
     */
    fun getAllWordGsons(context: Context, contentProviderApplicationId: String): List<WordGson> {
        Log.i(TAG, "getAllWordGsons")

        val wordGsons: MutableList<WordGson> = ArrayList()

        val wordsUri =
            Uri.parse("content://$contentProviderApplicationId.provider.word_provider/words")
        Log.i(TAG, "wordsUri: $wordsUri")
        val wordsCursor = context.contentResolver.query(wordsUri, null, null, null, null)
        Log.i(TAG, "wordsCursor: $wordsCursor")
        if (wordsCursor == null) {
            Log.e(TAG, "wordsCursor == null")
            Toast.makeText(context, "wordsCursor == null", Toast.LENGTH_LONG).show()
        } else {
            Log.i(
                TAG,
                "wordsCursor.getCount(): " + wordsCursor.count
            )
            if (wordsCursor.count == 0) {
                Log.e(TAG, "wordsCursor.getCount() == 0")
            } else {
                var isLast = false
                while (!isLast) {
                    wordsCursor.moveToNext()

                    // Convert from Room to Gson
                    val wordGson = CursorToWordGsonConverter.getWordGson(wordsCursor)

                    wordGsons.add(wordGson)

                    isLast = wordsCursor.isLast
                }

                wordsCursor.close()
                Log.i(
                    TAG,
                    "wordsCursor.isClosed(): " + wordsCursor.isClosed
                )
            }
        }
        Log.i(TAG, "wordGsons.size(): " + wordGsons.size)

        return wordGsons
    }

    fun getWordGson(
        wordId: Long,
        context: Context,
        contentProviderApplicationId: String
    ): WordGson? {
        Log.i(TAG, "getWordGson")

        var wordGson: WordGson? = null

        val wordUri =
            Uri.parse("content://$contentProviderApplicationId.provider.word_provider/words/$wordId")
        Log.i(TAG, "wordUri: $wordUri")
        val wordCursor = context.contentResolver.query(wordUri, null, null, null, null)
        Log.i(TAG, "wordCursor: $wordCursor")
        if (wordCursor == null) {
            Log.e(TAG, "wordCursor == null")
            Toast.makeText(context, "wordCursor == null", Toast.LENGTH_LONG).show()
        } else {
            Log.i(
                TAG,
                "wordCursor.getCount(): " + wordCursor.count
            )
            if (wordCursor.count == 0) {
                Log.e(TAG, "wordCursor.getCount() == 0")
            } else {
                wordCursor.moveToFirst()

                // Convert from Room to Gson
                wordGson = CursorToWordGsonConverter.getWordGson(wordCursor)

                wordCursor.close()
                Log.i(
                    TAG,
                    "wordCursor.isClosed(): " + wordCursor.isClosed
                )
            }
        }
        Log.i(TAG, "wordGson: $wordGson")

        return wordGson
    }

    fun getAllEmojiGsons(
        wordId: Long,
        context: Context,
        contentProviderApplicationId: String
    ): List<EmojiGson> {
        Log.i(TAG, "getAllEmojiGsons")

        val emojiGsons: MutableList<EmojiGson> = ArrayList()

        val emojisUri =
            Uri.parse("content://$contentProviderApplicationId.provider.emoji_provider/emojis/by-word-label-id/$wordId")
        Log.i(TAG, "emojisUri: $emojisUri")
        val emojisCursor = context.contentResolver.query(emojisUri, null, null, null, null)
        Log.i(TAG, "emojisCursor: $emojisCursor")
        if (emojisCursor == null) {
            Log.e(TAG, "emojisCursor == null")
            Toast.makeText(context, "emojisCursor == null", Toast.LENGTH_LONG).show()
        } else {
            Log.i(
                TAG,
                "emojisCursor.getCount(): " + emojisCursor.count
            )
            if (emojisCursor.count == 0) {
                Log.e(TAG, "emojisCursor.getCount() == 0")
            } else {
                var isLast = false
                while (!isLast) {
                    emojisCursor.moveToNext()

                    // Convert from Room to Gson
                    val emojiGson = CursorToEmojiGsonConverter.getEmojiGson(emojisCursor)

                    emojiGsons.add(emojiGson)

                    isLast = emojisCursor.isLast
                }

                emojisCursor.close()
                Log.i(
                    TAG,
                    "emojisCursor.isClosed(): " + emojisCursor.isClosed
                )
            }
        }
        Log.i(TAG, "emojiGsons.size(): " + emojiGsons.size)

        return emojiGsons
    }

    fun getImageGson(
        imageId: Long,
        context: Context,
        contentProviderApplicationId: String
    ): ImageGson? {
        Log.i(TAG, "getImageGson")

        var imageGson: ImageGson? = null

        val imageUri =
            Uri.parse("content://$contentProviderApplicationId.provider.image_provider/images/$imageId")
        Log.i(TAG, "imageUri: $imageUri")
        val imageCursor = context.contentResolver.query(imageUri, null, null, null, null)
        Log.i(TAG, "imageCursor: $imageCursor")
        if (imageCursor == null) {
            Log.e(TAG, "imageCursor == null")
            Toast.makeText(context, "imageCursor == null", Toast.LENGTH_LONG).show()
        } else {
            Log.i(
                TAG,
                "imageCursor.getCount(): " + imageCursor.count
            )
            if (imageCursor.count == 0) {
                Log.e(TAG, "imageCursor.getCount() == 0")
            } else {
                imageCursor.moveToFirst()

                // Convert from Room to Gson
                imageGson = CursorToImageGsonConverter.getImageGson(imageCursor)

                imageCursor.close()
                Log.i(
                    TAG,
                    "imageCursor.isClosed(): " + imageCursor.isClosed
                )
            }
        }
        Log.i(TAG, "imageGson: $imageGson")

        return imageGson
    }

    fun getAllStoryBookGsons(
        context: Context,
        contentProviderApplicationId: String
    ): List<StoryBookGson> {
        Log.i(TAG, "getAllStoryBookGsons")

        val storyBookGsons: MutableList<StoryBookGson> = ArrayList()

        val storyBooksUri =
            Uri.parse("content://$contentProviderApplicationId.provider.storybook_provider/storybooks")
        Log.i(
            TAG,
            "storyBooksUri: $storyBooksUri"
        )
        val storyBooksCursor = context.contentResolver.query(storyBooksUri, null, null, null, null)
        Log.i(
            TAG,
            "storyBooksCursor: $storyBooksCursor"
        )
        if (storyBooksCursor == null) {
            Log.e(TAG, "storyBooksCursor == null")
            Toast.makeText(context, "storyBooksCursor == null", Toast.LENGTH_LONG).show()
        } else {
            Log.i(
                TAG,
                "storyBooksCursor.getCount(): " + storyBooksCursor.count
            )
            if (storyBooksCursor.count == 0) {
                Log.e(TAG, "storyBooksCursor.getCount() == 0")
            } else {
                var isLast = false
                while (!isLast) {
                    storyBooksCursor.moveToNext()

                    // Convert from Room to Gson
                    val storyBookGson =
                        CursorToStoryBookGsonConverter.getStoryBookGson(storyBooksCursor)

                    storyBookGsons.add(storyBookGson)

                    isLast = storyBooksCursor.isLast
                }

                storyBooksCursor.close()
                Log.i(
                    TAG,
                    "storyBooksCursor.isClosed(): " + storyBooksCursor.isClosed
                )
            }
        }
        Log.i(TAG, "storyBookGsons.size(): " + storyBookGsons.size)

        return storyBookGsons
    }

    fun getStoryBookGson(
        storyBookId: Long,
        context: Context,
        contentProviderApplicationId: String
    ): StoryBookGson? {
        Log.i(TAG, "getStoryBookGson")

        var storyBookGson: StoryBookGson? = null

        val storyBookUri =
            Uri.parse("content://$contentProviderApplicationId.provider.storybook_provider/storybooks/$storyBookId")
        Log.i(TAG, "storyBookUri: $storyBookUri")
        val storyBookCursor = context.contentResolver.query(storyBookUri, null, null, null, null)
        Log.i(
            TAG,
            "storyBookCursor: $storyBookCursor"
        )
        if (storyBookCursor == null) {
            Log.e(TAG, "storyBookCursor == null")
            Toast.makeText(context, "storyBookCursor == null", Toast.LENGTH_LONG).show()
        } else {
            Log.i(
                TAG,
                "storyBookCursor.getCount(): " + storyBookCursor.count
            )
            if (storyBookCursor.count == 0) {
                Log.e(TAG, "storyBookCursor.getCount() == 0")
            } else {
                storyBookCursor.moveToFirst()

                // Convert from Room to Gson
                storyBookGson = CursorToStoryBookGsonConverter.getStoryBookGson(storyBookCursor)

                storyBookCursor.close()
                Log.i(
                    TAG,
                    "storyBookCursor.isClosed(): " + storyBookCursor.isClosed
                )
            }
        }
        Log.i(
            TAG,
            "storyBookGson: $storyBookGson"
        )

        return storyBookGson
    }

    fun getStoryBookChapterGsons(
        storyBookId: Long,
        context: Context,
        contentProviderApplicationId: String
    ): List<StoryBookChapterGson> {
        Log.i(TAG, "getStoryBookChapterGsons")

        val storyBookChapterGsons: MutableList<StoryBookChapterGson> = ArrayList()

        // Prepend cover image and book title as its own chapter
        val coverChapterGson = StoryBookChapterGson()
        val storyBookGson = getStoryBookGson(storyBookId, context, contentProviderApplicationId)
        if (storyBookGson!!.coverImage != null) {
            val coverImageGson =
                getImageGson(storyBookGson.coverImage.id, context, contentProviderApplicationId)
            coverChapterGson.image = coverImageGson
        }
        val coverParagraphGsons: MutableList<StoryBookParagraphGson> = ArrayList()
        val coverTitleParagraphGson = StoryBookParagraphGson()
        coverTitleParagraphGson.originalText = storyBookGson.title
        coverParagraphGsons.add(coverTitleParagraphGson)
        // TODO: add storybook description (if available)
        coverChapterGson.storyBookParagraphs = coverParagraphGsons
        storyBookChapterGsons.add(coverChapterGson)

        val storyBookChaptersUri =
            Uri.parse("content://$contentProviderApplicationId.provider.storybook_provider/storybooks/$storyBookId/chapters")
        Log.i(
            TAG,
            "storyBookChaptersUri: $storyBookChaptersUri"
        )
        val storyBookChaptersCursor =
            context.contentResolver.query(storyBookChaptersUri, null, null, null, null)
        Log.i(
            TAG,
            "storyBookChaptersCursor: $storyBookChaptersCursor"
        )
        if (storyBookChaptersCursor == null) {
            Log.e(TAG, "storyBookChaptersCursor == null")
            Toast.makeText(context, "storyBookChaptersCursor == null", Toast.LENGTH_LONG).show()
        } else {
            Log.i(
                TAG,
                "storyBookChaptersCursor.getCount(): " + storyBookChaptersCursor.count
            )
            if (storyBookChaptersCursor.count == 0) {
                Log.e(
                    TAG,
                    "storyBookChaptersCursor.getCount() == 0"
                )
            } else {
                var isLast = false
                while (!isLast) {
                    storyBookChaptersCursor.moveToNext()

                    // Convert from Room to Gson
                    val storyBookChapterGson =
                        CursorToStoryBookChapterGsonConverter.getStoryBookChapterGson(
                            storyBookChaptersCursor,
                            context,
                            contentProviderApplicationId
                        )

                    storyBookChapterGsons.add(storyBookChapterGson)

                    isLast = storyBookChaptersCursor.isLast
                }

                storyBookChaptersCursor.close()
                Log.i(
                    TAG,
                    "storyBookChaptersCursor.isClosed(): " + storyBookChaptersCursor.isClosed
                )
            }
        }
        Log.i(
            TAG,
            "storyBookChapterGsons.size(): " + storyBookChapterGsons.size
        )

        return storyBookChapterGsons
    }
}
