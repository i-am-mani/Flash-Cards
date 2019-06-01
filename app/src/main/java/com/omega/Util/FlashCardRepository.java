package com.omega.Util;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.omega.Database.FlashCardDatabase;
import com.omega.Database.FlashCards;
import com.omega.Database.FlashCardsDao;
import com.omega.Database.Groups;
import com.omega.Database.GroupsDao;

import java.util.List;

public class FlashCardRepository {

    private FlashCardsDao flashCardDao;
    private GroupsDao groupsDao;

    private LiveData<List<FlashCards>> allFlashCards;

    private LiveData<List<Groups>> allGroups;

    public FlashCardRepository(Application application) {
        FlashCardDatabase flashCardDatabase = FlashCardDatabase.getDatabase(application);
        flashCardDao = flashCardDatabase.flashCardDao();
        groupsDao = flashCardDatabase.groupDao();
    }


    public LiveData<List<FlashCards>> getAllFlashCards() {
        allFlashCards = flashCardDao.getAllFlashCards();
        return allFlashCards;
    }

    public LiveData<List<FlashCards>> getAllFlashCardsOfGroup(String groupName) {
        LiveData<List<FlashCards>> allFlashCardsOfGroup = flashCardDao.getAllFlashCardsOfGroup(groupName);
        return allFlashCardsOfGroup;
    }

    public LiveData<List<Groups>> getAllGroups() {
        allGroups = groupsDao.getAllGroups();
        return allGroups;
    }


    public void insertFlashCard(FlashCards flashCard) {
        new InsertFlashCardAsyncTask(flashCardDao).execute(flashCard);
    }

    public void insertGroup(Groups group) {
        new InsertGroupAsyncTask(groupsDao).execute(group);
    }

    public void updateGroup(Groups group){
        new UpdateGroupAsyncTask(groupsDao).execute(group);
    }

    public void updateFlashCards(FlashCards flashCards) {
        new UpdateFlashCardsAsyncTask(flashCardDao).execute(flashCards);
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
        GroupsDao dao;

        InsertGroupAsyncTask(GroupsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Groups... groups) {
            Log.d("Repository", "doInBackground: Inside Do in Background");
            dao.insertGroup(groups[0]);
            Log.d("Repository", "doInBackground: Added new group");
            return null;
        }
    }


    private static class UpdateGroupAsyncTask extends AsyncTask<Groups, Void, Void> {
        GroupsDao dao;

        UpdateGroupAsyncTask(GroupsDao groupsDao) {
            dao = groupsDao;
        }


        @Override
        protected Void doInBackground(Groups... groups) {
            dao.updateGroup(groups[0]);
            return null;
        }
    }

    private static class UpdateFlashCardsAsyncTask extends AsyncTask<FlashCards, Void, Void> {
        FlashCardsDao dao;

        UpdateFlashCardsAsyncTask(FlashCardsDao flashCardsDao) {
            dao = flashCardsDao;
        }


        @Override
        protected Void doInBackground(FlashCards... flashCards) {
            dao.updateFlashCard(flashCards[0]);
            return null;
        }
    }
}
