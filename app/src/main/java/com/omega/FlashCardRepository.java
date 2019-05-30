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


    public LiveData<List<Groups>> getAllGroups() {
        return allGroups;
    }


    public void insertFlashCard(FlashCards flashCard) {
        new InsertFlashCardAsyncTask(flashCardDao).execute(flashCard);
    }

    public void insertGroup(Groups group) {
        new InsertGroupAsyncTask(groupDao).execute(group);
    }

    public void updateGroup(Groups group){
        new UpdateGroupAsynTask(groupDao).execute(group);
    }

    public void updateFlashCards(FlashCards flashCards) {
        new UpdateFlashCardsAsynTask(flashCardDao).execute(flashCards);
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

    private static class InsertGroupAsyncTask extends AsyncTask<Groups,Void,Void>{
        GroupDao dao;

        InsertGroupAsyncTask(GroupDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Groups... groups) {
            dao.insertGroup(groups[0]);
            return null;
        }
    }


    private static class UpdateGroupAsynTask extends AsyncTask<Groups, Void, Void> {
        GroupDao dao;

        UpdateGroupAsynTask(GroupDao groupDao) {
            dao = groupDao;
        }


        @Override
        protected Void doInBackground(Groups... groups) {
            dao.updateGroup(groups[0]);
            return null;
        }
    }

    private static class UpdateFlashCardsAsynTask extends AsyncTask<FlashCards, Void, Void> {
        FlashCardsDao dao;

        UpdateFlashCardsAsynTask(FlashCardsDao flashCardsDao) {
            dao = flashCardsDao;
        }


        @Override
        protected Void doInBackground(FlashCards... flashCards) {
            dao.updateFlashCard(flashCards[0]);
            return null;
        }
    }



}
