package com.edusoft.smshelper.ui;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.model.MainCategoryRoom;
import com.edusoft.smshelper.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainCategory extends AppCompatActivity {

    private AppDatabase db;
    private ListView listView;
    private List<MainCategoryRoom> mainCategories;
    private List<String> list;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category);

        db = Room.databaseBuilder(MainCategory.this,
                AppDatabase.class, "numbers").build();

        Toolbar myToolbar = findViewById(R.id.my_toolbar_insert_main_cat);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Main Categories"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mainCategories = new ArrayList<>();
        list = new ArrayList<>();

        listView = findViewById(R.id.main_category_list);
        arrayAdapter = new ArrayAdapter<>(MainCategory.this, android.R.layout.simple_list_item_1, list);


        Thread custom =new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        mainCategories.addAll(db.userDao().getAllMainCategory());
                        for(int i=0; i<mainCategories.size(); i++)
                        {
                            list.add(mainCategories.get(i).name);
                        }
                        listView.setAdapter(arrayAdapter);
                    }
                }
        );
        custom.start();
        try {
            custom.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showActionsDialog(list.get(position), mainCategories.get(position).id);
                return false;
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showActionsDialog(final String name, final int id) {
        CharSequence colors[] = new CharSequence[]{"Edit"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent i = new Intent(MainCategory.this, UpdateMainCategory.class);
                    i.putExtra("name",name);
                    i.putExtra("id", id);
                    startActivityForResult(i, 4);
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = getIntent();
        finish();
        startActivity(intent);
        Toast.makeText(MainCategory.this, "Main category updated successfully", Toast.LENGTH_SHORT).show();

    }
}
