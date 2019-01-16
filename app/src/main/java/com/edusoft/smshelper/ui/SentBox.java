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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.SmsModelRoom;
import com.edusoft.smshelper.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class SentBox extends AppCompatActivity {

    ListView sent_listView;
    CustomAdapter customAdapter;

    private  List<SmsModelRoom> list;
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_box);

        Toolbar myToolbar = findViewById(R.id.my_toolbar_sent);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Sent Box"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(SentBox.this,
                AppDatabase.class, "numbers").build();

        list = new ArrayList<>();
        Thread custom =new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        list.addAll(db.userDao().getAll());
                    }
                }
        );
        custom.start();
        try {
            custom.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sent_listView = findViewById(R.id.sent_listView);
        customAdapter = new CustomAdapter(this, R.layout.sent_item, list);
        sent_listView.setAdapter(customAdapter);

        sent_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
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
        CharSequence colors[] = new CharSequence[]{"Resend", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent i = new Intent(SentBox.this, MainActivity.class);
                    i.putExtra("msg",list.get(position).message);
                    startActivity(i);
                } else {

                    deleteNote(position);

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

    private void deleteNote(final int id) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SmsModelRoom model = list.get(id);
                db.userDao().deleteSms(model);
                list.remove(id);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class CustomAdapter extends ArrayAdapter<SmsModelRoom>
    {

        Context mContext;
        public CustomAdapter( Context context, int resource, List<SmsModelRoom> categoryLists) {
            super(context, resource, categoryLists);
            this.mContext = context;
        }

        private static class ViewHolder
        {
            TextView msg, date;
        }

        private int lastPosition = -1;

        @NonNull
        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {


            SmsModelRoom dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.sent_item, parent, false);

                viewHolder.date = convertView.findViewById(R.id.sent_textview_date);
                //viewHolder.categories = convertView.findViewById(R.id.sent_textview_category);
                viewHolder.msg = convertView.findViewById(R.id.sent_textview_msg);

                result=convertView;
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;

            viewHolder.date.setText(dataModel._date);
            viewHolder.msg.setText(dataModel.message);

            return convertView;
        }
    }
}
