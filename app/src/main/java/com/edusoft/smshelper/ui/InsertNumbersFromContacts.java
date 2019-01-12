package com.edusoft.smshelper.ui;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
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
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.model.ListItemModel;

import java.util.ArrayList;

public class InsertNumbersFromContacts extends AppCompatActivity {

    ListView listView;
    private static CustomAdapter adapter;
    ArrayList<ListItemModel> arrayList;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_numbers_from_contacts);

        Toolbar myToolbar = findViewById(R.id.my_toolbar_insert);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Insert from contacts"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        arrayList = getContactList();
        listView = findViewById(R.id.contact_list_phone);
        adapter = new CustomAdapter(this, R.layout.contact_item_row, arrayList);
        listView.setAdapter(adapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //ListItemModel item = arrayList.get(position);

//                dialog = new Dialog(InsertNumbersFromContacts.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.add_fragment);
//                dialog.show();
                //showActionsDialog(position);
                Intent i =new Intent(InsertNumbersFromContacts.this, AddContact.class);
                i.putExtra("number", arrayList.get(position).getNumber());
                i.putExtra("name", arrayList.get(position).getName());
                startActivityForResult(i, 2);
                return false;
            }
        });
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};
        String[] s = { "India ", "Arica", "India ", "Arica", "India ", "Arica",
                "India ", "Arica", "India ", "Arica" };

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(InsertNumbersFromContacts.this,
                android.R.layout.simple_spinner_item, s);
        final Spinner sp = new Spinner(InsertNumbersFromContacts.this);
        Button btnTag = new Button(this);
        btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        btnTag.setText("Button");
        //btnTag.setId(R.id.new_number_set);

        sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sp.setAdapter(adp);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setView(sp);
        builder.setView(btnTag);
        builder.show();
    }

    private ArrayList<ListItemModel> getContactList() {
        ContentResolver cr = getContentResolver();
        ArrayList<ListItemModel> listItemModels = new ArrayList<>();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {



                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        listItemModels.add(new ListItemModel(name,phoneNo));
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        return listItemModels;
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

    public static class CustomAdapter extends ArrayAdapter<ListItemModel> implements View.OnClickListener{
        Context mContext;
        

        public CustomAdapter(@NonNull Context context, int resource, ArrayList<ListItemModel> items) {
            super(context, resource, items);
            mContext = context;

        }

        private static class ViewHolder {
           TextView name;
           TextView number;
        }

        @Override
        public void onClick(View v) {

        }



        private int lastPosition = -1;
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ListItemModel dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.contact_item_row, parent, false);
                viewHolder.name = convertView.findViewById(R.id.name_txt);
                viewHolder.number = convertView.findViewById(R.id.number_txt);


                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;

            viewHolder.name.setText(dataModel.getName());
            viewHolder.number.setText(dataModel.getNumber());

            // Return the completed view to render on screen
            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            Bundle extras = data.getExtras();

            if (extras != null) {

                boolean success = extras.getBoolean("success");

                if (success) {
                    Toast.makeText(InsertNumbersFromContacts.this, "Contact added successfuly", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
