package com.edusoft.smshelper.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class SmsModelRoom {
    @PrimaryKey(autoGenerate = true )
    public int id;

    @ColumnInfo(name = "catCodeId")
    public String catCodeId;

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "date")
    public String _date;
}
