package ai.elimu.content_provider.room.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ai.elimu.content_provider.room.dao.ImageDao;
import ai.elimu.content_provider.room.dao.StoryBookDao;
import ai.elimu.content_provider.room.entity.Image;
import ai.elimu.content_provider.room.entity.StoryBook;

@Database(entities = {Image.class, StoryBook.class}, version = 1)
public abstract class RoomDb extends RoomDatabase {

    public abstract ImageDao imageDao();

    public abstract StoryBookDao storyBookDao();

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
//                            .addMigrations(
//                                    MIGRATION_1_2
//                            )
                            .build();
                }
            }
        }
        return INSTANCE;
    }

//    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            Log.i(getClass().getName(), "migrate (1 --> 2)");
//            Calendar timestamp = Calendar.getInstance();
//            String sql = "ALTER TABLE StoryBookLearningEvent ADD COLUMN timestamp INTEGER NOT NULL DEFAULT " + timestamp.getTimeInMillis();
//            Log.i(getClass().getName(), "sql: " + sql);
//            database.execSQL(sql);
//        }
//    };
}
