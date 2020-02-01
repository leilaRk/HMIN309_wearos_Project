package com.example.joseph.hmin309_wear_leila;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewMessageActivity extends WearableActivity {

    TextView txtId, txtStudentId, txtGpsLong, txtGpsLat, txtMessage, tvDisplayClock;
    LinearLayout linearLayout,linearLayoutClock;
    final String TAG = "ViewMessageActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        setAmbientEnabled();
        Log.d(TAG, "onCreate()");
        Bundle bundle = getIntent().getExtras();
        DataMsg message = (DataMsg) bundle.get("message");
        Log.d(TAG, message.toJSON().toString());
        linearLayout = findViewById(R.id.displayLinearLayout);
        linearLayoutClock = findViewById(R.id.linearLayoutClock);
        tvDisplayClock = findViewById(R.id.tvDisplayClock);
        txtId = (TextView) findViewById(R.id.msg_id);
        txtStudentId = (TextView) findViewById(R.id.msg_student_id);
        txtGpsLong = (TextView) findViewById(R.id.msg_gps_long);
        txtGpsLat = (TextView) findViewById(R.id.msg_gps_lat);
        txtMessage = (TextView) findViewById(R.id.msg_message);

        txtId.setText(message.getId() + "");
        txtStudentId.setText(message.getStd_id() + "");
        txtGpsLong.setText(message.getGps_long() + "");
        txtGpsLat.setText(message.getGps_lat() + "");
        txtMessage.setText(message.getMessage() + "");

    }


}
