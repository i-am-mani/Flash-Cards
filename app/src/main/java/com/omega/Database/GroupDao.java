package com.omega.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GroupDao {

    @Insert
    public long insertGroup(Groups groups);

    @Update
    public void updateGroup(Groups group);

    @Query("SELECT * FROM Groups")
    public LiveData<List<Groups>> getAllGroups();
}
