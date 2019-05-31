package com.omega.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FlashCards.class, Groups.class},version = 2)
public abstract class FlashCardDatabase extends RoomDatabase {

    public abstract FlashCardsDao flashCardDao();
    public abstract GroupDao groupDao();

    private static volatile FlashCardDatabase INSTANCE;

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
