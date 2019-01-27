/* Tharuka Lakshan
 * SMS Helper
 * DATA ACCESS OBJECT (DAO)
 * All Database operations*/

package com.edusoft.smshelper.util;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.model.MainCategoryRoom;
import com.edusoft.smshelper.model.SmsModelRoom;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PostsDAO {


    /*
    * All Category Database operations
    * */
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

    @Query("SELECT * FROM CategoryModelRoom WHERE mainCatId=:id")
    List<CategoryModelRoom> getCatAllWhere(int id);

    @Query("SELECT COUNT(*) AS count FROM CategoryModelRoom ")
    int getCatCount();
//    --------------------------------------------------------------



    /*
    * All Contact Database operations
    * */
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
//    ------------------------------------------------------------------


    /*
    * SMS All Database operations
    * */
    @Insert
    void insertSms(SmsModelRoom post);

    @Delete
    int deleteSms(SmsModelRoom post);

    @Query("SELECT * FROM SmsModelRoom WHERE id= :id LIMIT 1")
    SmsModelRoom getSms(int id);

    @Query("SELECT * FROM SmsModelRoom")
    List<SmsModelRoom> getAll();
//    ----------------------------------------------------------------



    /*
        * Main Category all database operations
     */
    @Insert
    void insertMainCategory(MainCategoryRoom post);

    @Delete
    void deleteMainCategory(MainCategoryRoom post);

    @Query("SELECT * FROM MainCategoryRoom WHERE id=:id LIMIT 1")
    MainCategoryRoom getMainCat(int id);

    @Update
    void updateMainCategory(MainCategoryRoom post);

    @Query("SELECT * FROM MainCategoryRoom")
    List<MainCategoryRoom> getAllMainCategory();
//    -----------------------------------------------------------------



}
