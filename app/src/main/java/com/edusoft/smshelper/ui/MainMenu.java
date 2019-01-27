package com.edusoft.smshelper.ui;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.MainCategoryRoom;
import com.edusoft.smshelper.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sf;
    private AppDatabase db;
    List<MainCategoryRoom> mainCategory;
    List<CategoryModelRoom> category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        db = Room.databaseBuilder(MainMenu.this,
                AppDatabase.class, "numbers").build();
        mainCategory = new ArrayList<>();
        category = new ArrayList<>();

//        sf = getSharedPreferences("first_time", MODE_PRIVATE);
//        boolean first = sf.getBoolean("first", true);
//        sf.edit().putBoolean("first", false);
//        sf.edit().apply();

        mainCategory.add(new MainCategoryRoom("Main Category 1"));
        mainCategory.add(new MainCategoryRoom("Main Category 2"));
        mainCategory.add(new MainCategoryRoom("Main Category 3"));


        category.add(new CategoryModelRoom(getString(R.string.ministers), 1));
        category.add(new CategoryModelRoom(getString(R.string.list_canditates), 1));
        category.add(new CategoryModelRoom(getString(R.string.young_summit), 1));
        category.add(new CategoryModelRoom(getString(R.string.women_summit), 1));
        category.add(new CategoryModelRoom(getString(R.string.branch_summits), 1));

        category.add(new CategoryModelRoom(getString(R.string.business), 2));
        category.add(new CategoryModelRoom(getString(R.string.three_wheel), 2));
        category.add(new CategoryModelRoom(getString(R.string.self_employees), 2));
        category.add(new CategoryModelRoom(getString(R.string.dham_teachers), 2));
        category.add(new CategoryModelRoom(getString(R.string.pre_school), 2));
        category.add(new CategoryModelRoom(getString(R.string.farmers), 2));

        category.add(new CategoryModelRoom(getString(R.string.agri_offices), 3));
        category.add(new CategoryModelRoom(getString(R.string.samurdhi), 3));
        category.add(new CategoryModelRoom(getString(R.string.pension), 3));
        category.add(new CategoryModelRoom(getString(R.string.villedge_officer), 3));
        category.add(new CategoryModelRoom(getString(R.string.other), 3));

        Thread custom =new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int count = db.userDao().getCatCount();
                        if(count<=0) {
                            for(int i=0; mainCategory.size()>i; i++)
                            {
                                db.userDao().insertMainCategory(mainCategory.get(i));
                            }

                            for(int i=0; i<category.size(); i++)
                            {
                                db.userDao().insertCategory(category.get(i));
                            }
                        }
                    }
                }
        );
        custom.start();
        try {
            custom.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PLAYGROUND", "Permission is not granted, requesting");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS}, 123);
            // button.setEnabled(false);
        } else {
            Log.d("PLAYGROUND", "Permission is granted");
        }


        findViewById(R.id.send_sms).setOnClickListener(this);
        findViewById(R.id.insert_numbers_from_contact_list).setOnClickListener(this);
        findViewById(R.id.numbers_list).setOnClickListener(this);
        findViewById(R.id.category_insert).setOnClickListener(this);
        findViewById(R.id.sent_sms).setOnClickListener(this);
        findViewById(R.id.main_category_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId())
        {
            case R.id.send_sms:
                i = new Intent(MainMenu.this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.numbers_list:
                i = new Intent(MainMenu.this, NumbersList.class);
                startActivity(i);
                break;
            case R.id.insert_numbers_from_contact_list:
                i = new Intent(MainMenu.this, InsertNumbersFromContacts.class);
                startActivity(i);
                break;
            case R.id.category_insert:
                i = new Intent(MainMenu.this, CategoryList.class);
                startActivity(i);
                break;
            case R.id.sent_sms:
                i = new Intent(MainMenu.this, SentBox.class);
                startActivity(i);
                break;
            case R.id.main_category_button:
                i = new Intent(MainMenu.this, MainCategory.class);
                startActivity(i);
                break;
        }
    }
}
