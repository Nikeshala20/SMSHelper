package com.edusoft.smshelper.ui;

import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.model.ListItemModel;
import com.edusoft.smshelper.util.AppDatabase;
import com.edusoft.smshelper.util.CategoryHelper;

import java.util.ArrayList;

public class InsertNumbers extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_numbers);





    }



}
