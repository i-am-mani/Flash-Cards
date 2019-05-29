package com.omega.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FlashCardsDao {

    @Insert
    public void insertFlashCard(FlashCards... flashCards);

    @Delete
    public void deleteFlashCard(FlashCards card);

    @Query("SELECT * FROM Flashcards")
    public LiveData<List<FlashCards>> getAllFlashCards();
}
