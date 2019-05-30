package com.omega.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface GroupDao {

    @Insert
    public int insertGroup(Groups groups);

    @Update
    public void updateGroup(Groups group);

    @Query("SELECT * FROM Groups")
    public LiveData<Groups> getAllGroups();
}
