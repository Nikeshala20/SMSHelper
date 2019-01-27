package com.edusoft.smshelper.ui;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.MainCategoryRoom;
import com.edusoft.smshelper.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class InsertNewCategory extends AppCompatActivity {

    private AppDatabase db;
    private AppCompatSpinner spinner;
    private List<MainCategoryRoom> mainCategoryRooms;
    private List<String> catListNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_new_category);

        db = Room.databaseBuilder(InsertNewCategory.this,
                AppDatabase.class, "numbers").build();
        spinner = findViewById(R.id.insert_category_spinner);
        catListNames = new ArrayList<>();
        mainCategoryRooms = new ArrayList<>();

        final EditText editText = findViewById(R.id.new_category_edittext);
        Button button = findViewById(R.id.insert_category_button);

        Thread custom =new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        mainCategoryRooms.addAll(db.userDao().getAllMainCategory());
                        for(int i=0; mainCategoryRooms.size()>i; i++)
                        {
                            catListNames.add(mainCategoryRooms.get(i).name);
                        }

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(InsertNewCategory.this, android.R.layout.simple_spinner_item,
                                catListNames);
                        spinnerAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        spinner.setAdapter(spinnerAdapter);
                    }
                }
        );

        custom.start();
        try {
            custom.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        CategoryModelRoom model = new CategoryModelRoom(editText.getText().toString(), 1);
                        db.userDao().insertCategory(model);
                        Intent responseIntent = new Intent();
                        responseIntent.putExtra("success",true);
                        setResult(12,responseIntent);
                        finish();
                    }
                }.start();

                Toast.makeText(InsertNewCategory.this, "New category inserted successfully!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
