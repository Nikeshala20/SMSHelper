package com.edusoft.smshelper.util;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.model.MainCategoryRoom;
import com.edusoft.smshelper.model.SmsModelRoom;


@Database(entities = {MainCategoryRoom.class, CategoryModelRoom.class, ContactModelRoom.class, SmsModelRoom.class,}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PostsDAO userDao();
}