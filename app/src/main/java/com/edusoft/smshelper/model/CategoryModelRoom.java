package com.edusoft.smshelper.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = MainCategoryRoom.class,
        parentColumns = "id",
        childColumns = "mainCatId",
        onDelete = CASCADE))

public class CategoryModelRoom {
    @PrimaryKey(autoGenerate = true )
    public int id;

    @ColumnInfo(name = "mainCatId")
    public int mainCatId;

    @ColumnInfo(name = "name")
    public String name;

    public CategoryModelRoom(String name, int mainCatId) {
        this.name = name;
        this.mainCatId = mainCatId;
    }
}
