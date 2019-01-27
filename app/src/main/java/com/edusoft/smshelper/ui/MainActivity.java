package com.edusoft.smshelper.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.CategoryModelRoom;
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.model.MainCategoryRoom;
import com.edusoft.smshelper.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    public static ArrayList<Integer> result;
    Button button;
    ListView listView;
    private AppDatabase db;
    private static List<MainCategoryRoom> catList;
    private static CustomAdapter adapter;
    public static List<Boolean> isSelected;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar_send);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Send SMS"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button = findViewById(R.id.send_btn);
        isSelected = new ArrayList<>();

        catList = new ArrayList<>();
        db = Room.databaseBuilder(MainActivity.this,
                AppDatabase.class, "numbers").build();


        Thread custom =new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            catList.addAll(db.userDao().getAllMainCategory());
                            for(int i=0; i<catList.size(); i++)
                            {
                                isSelected.add(false);
                            }
                        }
                    }
                );
        custom.start();
        try {
            custom.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        listView = findViewById(R.id.category_list);
        adapter = new CustomAdapter(this, R.layout.send_item,catList);
        listView.setAdapter(adapter);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, SubCategory.class);
                i.putExtra("id", catList.get(position).id);
                startActivity(i);
               // view.
            }
        };
        listView.setOnItemClickListener(onItemClickListener);



        result = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Send.class);
                i.putIntegerArrayListExtra("numberlist",returnListCats());
                startActivityForResult(i, 5);

                //Toast.makeText(MainActivity.this, "Enabled",Toast.LENGTH_SHORT).show();
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(MainActivity.this, SubCategory.class);
//                i.putExtra("id", catList.get(position).id);
//                startActivity(i);
//                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private ArrayList<Integer> returnListCats()
    {
        ArrayList<Integer> results = new ArrayList<>();
        final List<CategoryModelRoom> catListAll = new ArrayList<>();

        for(int i=0; i<isSelected.size(); i++)
        {

            if(isSelected.get(i))
            {
                final int finalI = i;
                Thread custom =new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                catListAll.addAll(db.userDao().getCatAllWhere(catList.get(finalI).id));
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
            for(int j=0; j<catListAll.size(); j++)
            {
                results.add(catListAll.get(j).id);
            }

            //Toast.makeText(this, ""+results.size(), Toast.LENGTH_SHORT).show();

        }
        return results;
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


    public static class CustomAdapter extends ArrayAdapter<MainCategoryRoom> implements View.OnClickListener {
        Context mContext;

        public CustomAdapter( Context context, int resource, List<MainCategoryRoom> list) {
            super(context, resource, list);
            mContext = context;
        }

        @Override
        public void onClick(View v) {

        }
        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        private static class ViewHolder {
            TextView name;
            CheckBox checked;
        }

        private int lastPosition = -1;
        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MainCategoryRoom dataModel = getItem(position);
            ViewHolder viewHolder; // view lookup cache stored in tag
            final View result;
            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.send_item, parent, false);
                viewHolder.name = convertView.findViewById(R.id.textview_item);
                viewHolder.checked = convertView.findViewById(R.id.checkBox_item);
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
            viewHolder.name.setText(dataModel.name);
            viewHolder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        isSelected.set(position, true);
                    }
                    else
                    {
                        isSelected.set(position, false);
                    }
                }
            });

            return convertView;
        }
    }


}
