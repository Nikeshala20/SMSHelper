package com.edusoft.smshelper.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class MainCategoryRoom {
    @PrimaryKey(autoGenerate = true )
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    public MainCategoryRoom(String name) {
        this.name = name;
    }
}
