package com.omega.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FlashCardsDao {

    @Insert
    public void insertFlashCard(FlashCards... flashCards);

    @Update
    public void updateFlashCard(FlashCards flashCards);

    @Delete
    public void deleteFlashCard(FlashCards card);

    @Query("SELECT * FROM Flashcards")
    public LiveData<List<FlashCards>> getAllFlashCards();

    @Query("SELECT * FROM Flashcards WHERE group_name = :group_name")
    public LiveData<List<FlashCards>> getAllFlashCardsOfGroup(String group_name);

    @Query("SELECT * From Flashcards")
    public List<FlashCards> getAllFlashCardsAsList();
}
