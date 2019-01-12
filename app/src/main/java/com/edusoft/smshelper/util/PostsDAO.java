package com.edusoft.smshelper.util;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.model.SmsModelRoom;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PostsDAO {

    @Query("SELECT * FROM SmsModelRoom")
    List<SmsModelRoom> getAll();

    @Insert
    void insertCategory(CategoryModelRoom model);

    @Query("SELECT * FROM CategoryModelRoom WHERE id= :id LIMIT 1")
    CategoryModelRoom getCategory(int id);

    @Update
    void updateCategory(CategoryModelRoom post);

    @Delete
    void deleteCategory(CategoryModelRoom post);

    @Query("SELECT * FROM CategoryModelRoom")
    List<CategoryModelRoom> getCatAll();

    @Query("SELECT COUNT(*) AS count FROM CategoryModelRoom ")
    int getCatCount();

    @Insert
    long insertContact(ContactModelRoom post);

    @Delete
    void deleteContact(ContactModelRoom post);

    @Update
    void updateContact(ContactModelRoom post);

    @Query("SELECT * FROM ContactModelRoom")
    List<ContactModelRoom> getAllContacts();

    @Query("SELECT * FROM ContactModelRoom WHERE catCodeId=:id")
    List<ContactModelRoom> getContactsByCat(int id);

    @Query("SELECT * FROM ContactModelRoom WHERE id= :id LIMIT 1")
    ContactModelRoom getContact(int id);


    @Insert
    long insertSms(SmsModelRoom post);
    @Delete
    int deleteSms(SmsModelRoom post);


}
