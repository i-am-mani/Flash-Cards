package com.omega.Database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Groups")
public class Groups {

    @ColumnInfo(name = "created_at")
    private String createdAt;

    @PrimaryKey
    @ColumnInfo(name = "name")
    private String groupName;

    @ColumnInfo(name = "description")
    private String groupDescription;

    public Groups(String name,String description){
        groupName = name;
        groupDescription = description;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }





}
