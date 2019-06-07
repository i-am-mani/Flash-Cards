package com.omega.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {FlashCards.class, Groups.class}, version = 5)
public abstract class FlashCardDatabase extends RoomDatabase {

    public abstract FlashCardsDao flashCardDao();

    public abstract GroupsDao groupDao();

    private static volatile FlashCardDatabase INSTANCE;

    static final Migration MIGRATION_1_5 = new Migration(1, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public static FlashCardDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (FlashCardDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FlashCardDatabase.class, "db_flash_cards").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }


}
