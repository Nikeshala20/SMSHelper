package com.edusoft.smshelper.ui;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.MainCategoryRoom;
import com.edusoft.smshelper.util.AppDatabase;

public class UpdateMainCategory extends AppCompatActivity {

    Button button;
    EditText editText;
    String text;
    int id;
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_main_category);

        button = findViewById(R.id.edit_main_category_button);
        editText = findViewById(R.id.edit_main_category_editText);

        db = Room.databaseBuilder(UpdateMainCategory.this,
                AppDatabase.class, "numbers").build();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            text = extras.getString("name");
            id = extras.getInt("id");
        }

        editText.setText(text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().isEmpty())
                {
                    updateCat();
                }else
                {
                    Toast.makeText(UpdateMainCategory.this, "Please insert category name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateCat()
    {
        Thread custom =new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        MainCategoryRoom update_model = db.userDao().getMainCat(id);
                        update_model.name = editText.getText().toString();
                        db.userDao().updateMainCategory(update_model);
                        Intent responseIntent = new Intent();
                        responseIntent.putExtra("updated",true);
                        setResult(21,responseIntent);
                        finish();
                    }
                }
        );
        custom.start();
        try {
            custom.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
