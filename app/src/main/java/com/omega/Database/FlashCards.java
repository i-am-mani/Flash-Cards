package com.omega.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "Flashcards",
        indices = {@Index(value = {"title", "group_name"}, unique = true)},
        foreignKeys = {@ForeignKey(entity = Groups.class, parentColumns = "name", childColumns = "group_name", onDelete = CASCADE, onUpdate = CASCADE)})
public class FlashCards {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Ignore
    public FlashCards(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    @NonNull
    @ColumnInfo(name = "group_name")
    private String groupName;

    public FlashCards(String title, String content, String groupName) {
        this.title = title;
        this.content = content;
        this.groupName = groupName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
