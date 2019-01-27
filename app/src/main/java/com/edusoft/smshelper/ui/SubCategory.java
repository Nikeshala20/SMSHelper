package com.edusoft.smshelper.ui;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.MainCategoryRoom;
import com.edusoft.smshelper.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class SubCategory extends AppCompatActivity {

    int cat_id;
    ListView listView;
    public static ArrayList<Integer> resultList;
    private List<CategoryModelRoom> catList;
    private AppDatabase db;
    private CustomAdapter customAdapter;
    private Button sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        Toolbar myToolbar = findViewById(R.id.my_toolbar_sub_cat);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Send SMS Sub Category"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(this,
                AppDatabase.class, "numbers").build();

        listView = findViewById(R.id.sub_cat_list_view);
        sendButton = findViewById(R.id.send_btn_cat);


        catList = new ArrayList<>();
        resultList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cat_id = extras.getInt("id");
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubCategory.this, Send.class);
                i.putIntegerArrayListExtra("numberlist",resultList);
                startActivityForResult(i, 5);
            }
        });

        Thread custom =new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        catList.addAll(db.userDao().getCatAllWhere(cat_id));
                    }
                }
        );
        custom.start();
        try {
            custom.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        customAdapter = new CustomAdapter(this, R.layout.send_item,catList);
        listView.setAdapter(customAdapter);

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

    public static class CustomAdapter extends ArrayAdapter<CategoryModelRoom>
    {
        Context mContext;

        public CustomAdapter(Context context, int resource, List<CategoryModelRoom> list) {
            super(context, resource, list);
            this.mContext = context;
        }

        private static class ViewHolder
        {
            TextView categoryName;
            CheckBox isSelected;
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        private int lastPosition = -1;
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final CategoryModelRoom dataModel = getItem(position);
            ViewHolder viewHolder;
            View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.send_item, parent, false);
                viewHolder.categoryName = convertView.findViewById(R.id.textview_item);
                viewHolder.isSelected = convertView.findViewById(R.id.checkBox_item);
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
            viewHolder.categoryName.setText(dataModel.name);
            viewHolder.isSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        if(MainActivity.result.size()==0)
                        {
                            resultList.add(dataModel.id);
                        }
                        else
                        {
                            for(int i=0; i<MainActivity.result.size(); i++)
                            {
                                if(MainActivity.result.get(i)!= dataModel.id)
                                {
                                    MainActivity.result.add(dataModel.id);
                                }
                            }
                        }

                    }else
                    {
                        if(MainActivity.result.size()!=0) {
                            for (int i = 0; i < MainActivity.result.size(); i++) {
                                //Toast.makeText(mContext, "available", Toast.LENGTH_SHORT).show();
                                if (MainActivity.result.get(i) == dataModel.id) {
                                    MainActivity.result.remove(i);
                                }
                            }
                        }
                    }
                }
            });


            return convertView;
        }
    }
}
