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
import com.edusoft.smshelper.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sf;
    private AppDatabase db;
    List<Integer> catList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        db = Room.databaseBuilder(MainMenu.this,
                AppDatabase.class, "numbers").build();

//        sf = getSharedPreferences("first_time", MODE_PRIVATE);
//        boolean first = sf.getBoolean("first", true);
//        sf.edit().putBoolean("first", false);
//        sf.edit().apply();

        catList = new ArrayList<>();
        catList.add(R.string.ministers);
        catList.add(R.string.list_canditates);
        catList.add(R.string.young_summit);
        catList.add(R.string.women_summit);
        catList.add(R.string.branch_summits);
        catList.add(R.string.business);
        catList.add(R.string.three_wheel);
        catList.add(R.string.self_employees);
        catList.add(R.string.dham_teachers);
        catList.add(R.string.pre_school);
        catList.add(R.string.farmers);
        catList.add(R.string.agri_offices);
        catList.add(R.string.samurdhi);
        catList.add(R.string.pension);
        catList.add(R.string.villedge_officer);
        catList.add(R.string.other);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PLAYGROUND", "Permission is not granted, requesting");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS}, 123);
            // button.setEnabled(false);
        } else {
            Log.d("PLAYGROUND", "Permission is granted");
        }

        new Thread()
        {
            @Override
            public void run() {
                super.run();
                int count = db.userDao().getCatCount();
                if(count<=0)
                {
                    for(int i=0; i<catList.size(); i++)
                    {
                        CategoryModelRoom model = new CategoryModelRoom(getString(catList.get(i)));
                        db.userDao().insertCategory(model);
                    }
                }
            }
        }.start();

        findViewById(R.id.send_sms).setOnClickListener(this);
        findViewById(R.id.insert_numbers_from_contact_list).setOnClickListener(this);
        findViewById(R.id.numbers_list).setOnClickListener(this);
        findViewById(R.id.category_insert).setOnClickListener(this);
        findViewById(R.id.sent_sms).setOnClickListener(this);

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
        }
    }
}
