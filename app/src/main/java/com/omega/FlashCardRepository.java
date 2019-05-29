package com.omega;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.omega.Database.*;

import java.util.List;

public class FlashCardRepository {

    private FlashCardsDao flashCardDao;
    private GroupDao groupDao;

    private LiveData<List<FlashCards>> allFlashCards;
    private LiveData<List<Groups>> allGroups;

    public FlashCardRepository(Application application) {
        FlashCardDatabase flashCardDatabase = FlashCardDatabase.getDatabase(application);

        flashCardDao = flashCardDatabase.flashCardDao();
        groupDao = flashCardDatabase.groupDao();

        allFlashCards = flashCardDao.getAllFlashCards();
        groupDao.getAllGroups();
    }


    public LiveData<List<FlashCards>> getAllFlashCards() {
        return allFlashCards;
    }

    public void insertFlashCard(FlashCards flashCard) {
        new InsertFlashCardAsyncTask(flashCardDao).execute(flashCard);
    }


    private static class InsertFlashCardAsyncTask extends AsyncTask<FlashCards, Void, Void> {
        FlashCardsDao dao;

        InsertFlashCardAsyncTask(FlashCardsDao flashCardDao){
            dao = flashCardDao;
        }

        @Override
        protected Void doInBackground(FlashCards... flashCards) {
            dao.insertFlashCard(flashCards[0]);
            return null;
        }

    }

    private static class InsertGroupAsynTask extends AsyncTask<Groups,Void,Void>{
        GroupDao dao;

        InsertGroupAsynTask(GroupDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Groups... groups) {
            dao.insertGroup(groups[0]);
            return null;
        }
    }


}
