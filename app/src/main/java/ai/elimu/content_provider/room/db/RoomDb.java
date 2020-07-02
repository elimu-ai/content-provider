package ai.elimu.content_provider.room.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ai.elimu.content_provider.room.dao.EmojiDao;
import ai.elimu.content_provider.room.dao.Emoji_WordDao;
import ai.elimu.content_provider.room.dao.ImageDao;
import ai.elimu.content_provider.room.dao.Image_WordDao;
import ai.elimu.content_provider.room.dao.LetterDao;
import ai.elimu.content_provider.room.dao.StoryBookChapterDao;
import ai.elimu.content_provider.room.dao.StoryBookDao;
import ai.elimu.content_provider.room.dao.StoryBookParagraphDao;
import ai.elimu.content_provider.room.dao.StoryBookParagraph_WordDao;
import ai.elimu.content_provider.room.dao.WordDao;
import ai.elimu.content_provider.room.entity.Emoji;
import ai.elimu.content_provider.room.entity.Emoji_Word;
import ai.elimu.content_provider.room.entity.Image;
import ai.elimu.content_provider.room.entity.Image_Word;
import ai.elimu.content_provider.room.entity.Letter;
import ai.elimu.content_provider.room.entity.StoryBook;
import ai.elimu.content_provider.room.entity.StoryBookChapter;
import ai.elimu.content_provider.room.entity.StoryBookParagraph;
import ai.elimu.content_provider.room.entity.StoryBookParagraph_Word;
import ai.elimu.content_provider.room.entity.Word;

@Database(version = 16, entities = {Letter.class, Word.class, Emoji.class, Emoji_Word.class, Image.class, Image_Word.class, StoryBook.class, StoryBookChapter.class, StoryBookParagraph.class, StoryBookParagraph_Word.class})
@TypeConverters({Converters.class})
public abstract class RoomDb extends RoomDatabase {

    public abstract LetterDao letterDao();

    public abstract WordDao wordDao();

    public abstract EmojiDao emojiDao();

    public abstract Emoji_WordDao emoji_WordDao();

    public abstract ImageDao imageDao();

    public abstract Image_WordDao image_WordDao();

    public abstract StoryBookDao storyBookDao();

    public abstract StoryBookChapterDao storyBookChapterDao();

    public abstract StoryBookParagraphDao storyBookParagraphDao();

    public abstract StoryBookParagraph_WordDao storyBookParagraph_WordDao();

    private static volatile RoomDb INSTANCE;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static RoomDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(
                                    context.getApplicationContext(),
                                    RoomDb.class,
                                    "content_provider_db"
                            )
                            .addMigrations(
                                    MIGRATION_1_2,
                                    MIGRATION_2_3,
                                    MIGRATION_3_4,
                                    MIGRATION_8_9,
                                    MIGRATION_9_10,
                                    MIGRATION_11_12,
                                    MIGRATION_12_13,
                                    MIGRATION_13_14,
                                    MIGRATION_14_15,
                                    MIGRATION_15_16
                            )
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.i(getClass().getName(), "migrate (1 --> 2)");
            String sql = "ALTER TABLE Image ADD COLUMN `imageFormat` TEXT NOT NULL DEFAULT 'PNG'";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.i(getClass().getName(), "migrate (2 --> 3)");
            String sql = "ALTER TABLE StoryBook ADD COLUMN `coverImageId` INTEGER NOT NULL DEFAULT 0";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.i(getClass().getName(), "migrate (3 --> 4)");
            String sql = "DELETE FROM StoryBook";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);
        }
    };

    private static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.i(getClass().getName(), "migrate (8 --> 9)");
            String sql = "CREATE TABLE IF NOT EXISTS `Word` (`text` TEXT NOT NULL, `revisionNumber` INTEGER NOT NULL, `id` INTEGER, PRIMARY KEY(`id`))";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);
        }
    };

    private static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.i(getClass().getName(), "migrate (9 --> 10)");
            String sql = "CREATE TABLE IF NOT EXISTS `StoryBookParagraph_Word` (`StoryBookParagraph_id` INTEGER NOT NULL, `words_id` INTEGER NOT NULL, `words_ORDER` INTEGER NOT NULL, PRIMARY KEY(`StoryBookParagraph_id`, `words_id`))";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);
        }
    };

    private static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.i(getClass().getName(), "migrate (11 --> 12)");
            String sql = "ALTER TABLE `Word` ADD COLUMN `wordType` TEXT";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);
        }
    };

    private static final Migration MIGRATION_12_13 = new Migration(12, 13) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.i(getClass().getName(), "migrate (12 --> 13)");

            String sql = "ALTER TABLE `Image` ADD COLUMN `usageCount` INTEGER";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);

            sql = "ALTER TABLE `StoryBook` ADD COLUMN `usageCount` INTEGER";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);

            sql = "ALTER TABLE `Word` ADD COLUMN `usageCount` INTEGER";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);
        }
    };

    private static final Migration MIGRATION_13_14 = new Migration(13, 14) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.i(getClass().getName(), "migrate (13 --> 14)");

            String sql = "CREATE TABLE IF NOT EXISTS `Image_Word` (`Image_id` INTEGER NOT NULL, `words_id` INTEGER NOT NULL, PRIMARY KEY(`Image_id`, `words_id`))";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);
        }
    };

    private static final Migration MIGRATION_14_15 = new Migration(14, 15) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.i(getClass().getName(), "migrate (14 --> 15)");

            String sql = "CREATE TABLE IF NOT EXISTS `Emoji` (`glyph` TEXT NOT NULL, `unicodeVersion` REAL NOT NULL, `unicodeEmojiVersion` REAL NOT NULL, `revisionNumber` INTEGER NOT NULL, `usageCount` INTEGER, `id` INTEGER, PRIMARY KEY(`id`))";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);

            sql = "CREATE TABLE IF NOT EXISTS `Emoji_Word` (`Emoji_id` INTEGER NOT NULL, `words_id` INTEGER NOT NULL, PRIMARY KEY(`Emoji_id`, `words_id`))";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);
        }
    };

    private static final Migration MIGRATION_15_16 = new Migration(15, 16) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.i(getClass().getName(), "migrate (15 --> 16)");

            String sql = "CREATE TABLE IF NOT EXISTS `Letter` (`text` TEXT, `diacritic` INTEGER, `revisionNumber` INTEGER NOT NULL, `usageCount` INTEGER, `id` INTEGER, PRIMARY KEY(`id`))";
            Log.i(getClass().getName(), "sql: " + sql);
            database.execSQL(sql);
        }
    };
}
