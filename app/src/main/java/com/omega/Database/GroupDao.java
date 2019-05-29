package com.omega.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GroupDao {

    @Insert
    public int insertGroup(Groups groups);

    @Query("SELECT * FROM Groups")
    public LiveData<Groups> getAllGroups();
}
