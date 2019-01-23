package com.edusoft.smshelper.ui;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edusoft.smshelper.R;
import com.edusoft.smshelper.model.ContactModelRoom;
import com.edusoft.smshelper.model.SmsModelRoom;
import com.edusoft.smshelper.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Send extends AppCompatActivity {
    private AppDatabase db;
    ArrayList<Integer> result;
    EditText editText;
    Button button;
    private ProgressBar progressBar;
    Integer count =1;
    List<ContactModelRoom> numberList;
    private Handler progressBarHandler = new Handler();
    private String message = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        result = new ArrayList<>();
        db = Room.databaseBuilder(Send.this,
                AppDatabase.class, "numbers").build();
        numberList = new ArrayList<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        editText = findViewById(R.id.text_send);
        button = findViewById(R.id.send_button_send);

        progressBar =new ProgressBar(this,
                null,
                android.R.attr.progressBarStyleHorizontal);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            result = extras.getIntegerArrayList("numberlist");
            message = extras.getString("msg");
        }

        editText.setText(message);

        for(int i=0; i<result.size(); i++)
        {
            final int finalI = i;
            assert result != null;

            final int finalI1 = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    numberList.addAll(db.userDao().getContactsByCat(result.get(finalI1)));
                }
            };
            executorService.execute(runnable);

        }
        executorService.shutdown();
        while (!executorService.isTerminated()){}
        //Toast.makeText(Send.this, numberList.size()+"",Toast.LENGTH_SHORT).show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().isEmpty())
                {
                    new SendSMSTask(Send.this).execute();
                }else
                {
                    Toast.makeText(Send.this, "Please type your message", Toast.LENGTH_SHORT).show();
                }
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

    public class SendSMSTask extends AsyncTask<Void, Integer, String>{
        private ProgressDialog dialog;

        public SendSMSTask(Send activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected String doInBackground(Void... params) {

            SmsModelRoom smsModelRoom = new SmsModelRoom();
            smsModelRoom._date = new java.util.Date().toString();
            smsModelRoom.message = editText.getText().toString();

            db.userDao().insertSms(smsModelRoom);

            for(int i=0; i<numberList.size(); i++)
            {
                sendSMS(numberList.get(i).number, editText.getText().toString());
            }
            return null;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Doing something, please wait.");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
                finish();
            }
        }

        public void sendSMS(String phoneNo, String msg) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            } catch (Exception ignored) {

            }
        }

    }

}

