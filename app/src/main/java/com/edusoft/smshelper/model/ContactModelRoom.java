package com.edusoft.smshelper.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(foreignKeys = @ForeignKey(entity = CategoryModelRoom.class,
        parentColumns = "id",
        childColumns = "catCodeId",
        onDelete = CASCADE))
public class ContactModelRoom {
    @PrimaryKey(autoGenerate = true )
    public int id;

    @ColumnInfo(name = "catCodeId")
    public int catCodeId;

    @ColumnInfo(name = "number")
    public String number;

    @ColumnInfo(name = "name")
    public String name;

    public ContactModelRoom(int catCodeId, String number, String name) {
        this.catCodeId = catCodeId;
        this.number = number;
        this.name = name;
    }
}
