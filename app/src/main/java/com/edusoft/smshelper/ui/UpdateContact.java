package com.edusoft.smshelper.ui;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class UpdateContact extends AppCompatActivity {

    int cat, id;
    String number;

    Spinner spinner;
    EditText editText;
    Button button;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private AppDatabase db;
    private List<CategoryModelRoom> catList;
    private List<String> catListNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        spinner = findViewById(R.id.category_spinner_update);
        editText = findViewById(R.id.phone_number_update);
        button = findViewById(R.id.update_to_list);
        catListNames =new ArrayList<>();

        db = Room.databaseBuilder(UpdateContact.this,
                AppDatabase.class, "numbers").build();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                catList = db.userDao().getCatAll();

                for(int i=0; i<catList.size(); i++)
                {
                    catListNames.add(catList.get(i).name);
                }

                spinnerArrayAdapter = new ArrayAdapter<>
                        (UpdateContact.this, android.R.layout.simple_spinner_item,
                                catListNames); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            number = extras.getString("number");
            cat = extras.getInt("category");
            id = extras.getInt("id");
        }

        spinner.setSelection(cat);
        editText.setText(number);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        ContactModelRoom model = db.userDao().getContact(id);
                        model.catCodeId = catList.get(spinner.getSelectedItemPosition()).id;
                        model.number = editText.getText().toString();

                        db.userDao().updateContact(model);
                        Intent responseIntent = new Intent();
                        responseIntent.putExtra("success",true);
                        setResult(4,responseIntent);
                        finish();
                    }
                }.start();
            }
        });
    }
}
