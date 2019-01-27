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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class NumbersList extends AppCompatActivity {

    EditText editText, new_phone_name;
    Button button;
    Spinner spinner;
    private AppDatabase db;

    ListView listView;
    private static CustomAdapter adapter;
    List<ContactModelRoom> arrayList;
    private List<CategoryModelRoom> catList;
    private List<String> catListNames;
    private ArrayAdapter<String> spinnerArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers_list);
        db = Room.databaseBuilder(NumbersList.this,
                AppDatabase.class, "numbers").build();

        Toolbar myToolbar = findViewById(R.id.my_toolbar_list);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Numbers list"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        arrayList = new ArrayList<>();
        catListNames = new ArrayList<>();
        catList = new ArrayList<>();

        listView = findViewById(R.id.insert_list_view);

        editText = findViewById(R.id.new_phone_number);
        new_phone_name = findViewById(R.id.new_phone_name);
        button = findViewById(R.id.new_insert);
        spinner = findViewById(R.id.category_spinner);

        loadAll();

       // arrayList.add(new ContactModelRoom(0, "#444#"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(new_phone_name.getText().toString().isEmpty() || editText.getText().toString().isEmpty()))
                {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            db.userDao().insertContact(new ContactModelRoom(catList.get(spinner.getSelectedItemPosition()).id,editText.getText().toString(), new_phone_name.getText().toString()));
                            arrayList.add(new ContactModelRoom(catList.get(spinner.getSelectedItemPosition()).id,editText.getText().toString(), new_phone_name.getText().toString()));
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(NumbersList.this, "Number saved successfully!", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(NumbersList.this, "Please enter values", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showActionsDialog(position);
                //Toast.makeText(NumbersList.this, "Clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    private void loadAll()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                arrayList.addAll(db.userDao().getAllContacts());
                catList = db.userDao().getCatAll();

                for(int i=0; i<catList.size(); i++)
                {
                    catListNames.add(catList.get(i).name);
                }

                spinnerArrayAdapter = new ArrayAdapter<>
                        (NumbersList.this, android.R.layout.simple_spinner_item,
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

        adapter = new CustomAdapter(this, R.layout.contact_item_row, arrayList, catList);
        listView.setAdapter(adapter);
    }



    public static class CustomAdapter extends ArrayAdapter<ContactModelRoom> implements View.OnClickListener{
        Context mContext;
        List<CategoryModelRoom> catList;


        public CustomAdapter(@NonNull Context context, int resource, List<ContactModelRoom> items, List<CategoryModelRoom> cats) {
            super(context, resource, items);
            mContext = context;
            catList = cats;

        }

        private static class ViewHolder {
            TextView name;
            TextView number;
            TextView name_actual;
        }

        @Override
        public void onClick(View v) {

        }

        private int lastPosition = -1;
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ContactModelRoom dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.contact_item_row, parent, false);
                viewHolder.name = convertView.findViewById(R.id.name_txt);
                viewHolder.number = convertView.findViewById(R.id.number_txt);
                viewHolder.name_actual = convertView.findViewById(R.id.number_name);


                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;

            assert dataModel != null;
            try
            {
                viewHolder.name.setText(getCategory(dataModel.catCodeId));
            }catch (Exception e)
            {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            viewHolder.number.setText(dataModel.number);
            viewHolder.name_actual.setText(dataModel.name);

            Log.e("Created", "it");
            // Return the completed view to render on screen
            return convertView;
        }

        private String getCategory(int dmId)
        {
            for(int i=0; i<catList.size(); i++)
            {
                if(catList.get(i).id == dmId)
                {
                    return catList.get(i).name;
                }
            }
            return null;
        }

    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                   Intent i = new Intent(NumbersList.this, UpdateContact.class);
                   i.putExtra("category",position);
                   i.putExtra("id",arrayList.get(position).id);
                   i.putExtra("number", arrayList.get(position).number);
                   startActivityForResult(i, 4);
                } else {
                    deleteNote(position);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        builder.show();
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
    private void deleteNote(final int position)
    {
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                ContactModelRoom model = arrayList.get(position);
                db.userDao().deleteContact(model);
                arrayList.remove(position);
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==4)
        {
            Bundle extras = data.getExtras();

            if (extras != null) {

                boolean success = extras.getBoolean("success");

                if (success) {
                    Toast.makeText(NumbersList.this, "Contact updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        }
    }
}
