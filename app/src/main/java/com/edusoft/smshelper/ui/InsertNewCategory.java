package com.edusoft.smshelper.ui;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.util.AppDatabase;

public class InsertNewCategory extends AppCompatActivity {
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_new_category);

        db = Room.databaseBuilder(InsertNewCategory.this,
                AppDatabase.class, "numbers").build();
        final EditText editText = findViewById(R.id.new_category_edittext);
        Button button = findViewById(R.id.insert_category_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        CategoryModelRoom model = new CategoryModelRoom(editText.getText().toString());
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
