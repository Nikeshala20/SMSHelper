package com.edusoft.smshelper.ui;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddContact extends AppCompatActivity {

    Spinner spinner;
    Button button;
    String number;
    String name;
    private List<CategoryModelRoom> catList;
    private List<String> catListNames;
    private AppDatabase db;
    private ArrayAdapter<String> spinnerArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        spinner = findViewById(R.id.category_spinner_add);
        button = findViewById(R.id.insert_to_list);

        db = Room.databaseBuilder(AddContact.this,
                AppDatabase.class, "numbers").build();

        catList = new ArrayList<>();
        catListNames = new ArrayList<>();
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                 catList = db.userDao().getCatAll();

                 catListNames.clear();
                 for(int i=0; i<catList.size(); i++)
                 {
                    catListNames.add(catList.get(i).name);
                 }

                spinnerArrayAdapter = new ArrayAdapter<>
                        (AddContact.this, android.R.layout.simple_spinner_item,
                                catListNames); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
            }
        }.start();





        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            number = extras.getString("number");
            name = extras.getString("name");
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        db.userDao().insertContact(new ContactModelRoom(catList.get(spinner.getSelectedItemPosition()).id, number, name));
                        Intent responseIntent = new Intent();
                        responseIntent.putExtra("success",true);
                        setResult(2,responseIntent);
                        finish();
                    }
                }.start();
            }
        });
    }
}
