package com.edusoft.smshelper.ui;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class CategoryList extends AppCompatActivity {
    private AppDatabase db;
    private ListView listView;
    private Button button;
    private CustomAdapter customAdapter;
    private  List<CategoryModelRoom> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        db = Room.databaseBuilder(CategoryList.this,
                AppDatabase.class, "numbers").build();

        list = new ArrayList<>();
        Thread custom =new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        list.addAll(db.userDao().getCatAll());
                    }
                }
        );
        custom.start();
        try {
            custom.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        listView = findViewById(R.id.category_listview);
        button = findViewById(R.id.add_new_category_button);
        customAdapter = new CustomAdapter(this, R.layout.category_list_row_item, list);
        listView.setAdapter(customAdapter);

        Toolbar myToolbar = findViewById(R.id.my_toolbar_insert_category);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Category list"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryList.this, InsertNewCategory.class);
                startActivityForResult(i, 12);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showActionsDialog(position);
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
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent i = new Intent(CategoryList.this, EditCategory.class);
                    i.putExtra("category",list.get(position).name);
                    i.putExtra("id",list.get(position).id);
                    startActivityForResult(i, 7);
                } else {
                    try {
                        deleteNote(position);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        builder.show();
    }

    private void deleteNote(final int position) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CategoryModelRoom model = list.get(position);
                db.userDao().deleteCategory(model);
                list.remove(position);
            }
        });
        thread.start();
        thread.join();
    }

    public static class CustomAdapter extends ArrayAdapter<CategoryModelRoom>
    {

        Context mContext;
        public CustomAdapter(Context context, int resource, List<CategoryModelRoom> categoryLists) {
            super(context, resource, categoryLists);
            this.mContext = context;
        }

        private static class ViewHolder
        {
            TextView textView;
        }

        private int lastPosition = -1;
        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {
            CategoryModelRoom dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.category_list_row_item, parent, false);

                viewHolder.textView = convertView.findViewById(R.id.textview_item_category);

                result=convertView;

                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;

            viewHolder.textView.setText(dataModel.name);

            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==12)
        {
            Bundle extras = data.getExtras();

            if (extras != null) {

                boolean success = extras.getBoolean("success");

                if (success) {
                    Toast.makeText(CategoryList.this, "category added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }

            }
        }else
        {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            Toast.makeText(CategoryList.this, "category updated successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
