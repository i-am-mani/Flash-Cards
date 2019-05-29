package com.omega;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.omega.Database.FlashCards;

import java.util.List;

public class FlashCardViewModel extends AndroidViewModel {

    private FlashCardRepository flashCardRepository;

    private LiveData<List<FlashCards>> allFlashCard;

    public FlashCardViewModel(Application application) {
        super(application);
        flashCardRepository = new FlashCardRepository(application);
        allFlashCard = flashCardRepository.getAllFlashCards();
    }

    public LiveData<List<FlashCards>> getAllFlashCard() {
        return allFlashCard;
    }

    public void createFlashCard(String title,String content,String group){

    }


}
