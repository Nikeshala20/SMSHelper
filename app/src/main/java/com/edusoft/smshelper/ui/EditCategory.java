package com.edusoft.smshelper.ui;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.util.AppDatabase;

public class EditCategory extends AppCompatActivity {

    EditText editText;
    Button button;
    int id;
    String name;
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        editText = findViewById(R.id.edit_category_editText);
        button = findViewById(R.id.edit_category_button);

        db = Room.databaseBuilder(EditCategory.this,
                AppDatabase.class, "numbers").build();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            name = extras.getString("category");
        }
        editText.setText(name);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        CategoryModelRoom model = db.userDao().getCategory(id);
                        model.name = editText.getText().toString();
                        db.userDao().updateCategory(model);
                        Intent responseIntent = new Intent();
                        responseIntent.putExtra("updated",true);
                        setResult(7,responseIntent);
                        finish();
                    }
                }.start();
            }
        });
    }
}
