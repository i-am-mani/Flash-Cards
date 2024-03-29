package com.omega.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GroupsDao {

    @Insert
    public long insertGroup(Groups groups);

    @Update
    public void updateGroup(Groups group);

    @Query("SELECT * FROM Groups")
    public LiveData<List<Groups>> getAllGroups();

    @Delete
    void deleteGroup(Groups groups);

//    @Query("DELETE from Groups where name = :name")
//    void deleteGroup(String name);
}
