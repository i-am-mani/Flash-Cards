package com.omega.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Flashcards",
        indices = {@Index("group_name")},
        primaryKeys = {"title","group_name"},
        foreignKeys= {@ForeignKey(entity = Groups.class, parentColumns = "name", childColumns = "group_name")})
public class FlashCards {

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
