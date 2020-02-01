package com.example.joseph.hmin309_wear_leila;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.content.Context;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowMessagesActivity extends WearableActivity {
    WearableRecyclerView messagesRecyclerView;
    LinearLayout ambientLayout;
    ArrayList<DataMsg> messages;
    RequestQueue requestQueue;
    String url ="https://hmin309-embedded-systems.herokuapp.com/message-exchange/messages/";
    final String TAG = "ViewMessageListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_messages);
        Log.d(TAG, "onCreate");
        setAmbientEnabled();
        ambientLayout = findViewById(R.id.showAmbientLayout);
        messagesRecyclerView = findViewById(R.id.msgUpdatedView);
        //initialize the recyclerView
        initRecyclerView();
        //initialize the sensor
        setAmbientEnabled();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        messagesRecyclerView.setVisibility(View.GONE);
        ambientLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        getMessagesFromServer();
        messagesRecyclerView.setVisibility(View.VISIBLE);
        ambientLayout.setVisibility(View.GONE);
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView");
        messages = new ArrayList<DataMsg>();
        //getting the data from the server
        requestQueue = Volley.newRequestQueue(this);
        messagesRecyclerView.setEdgeItemsCenteringEnabled(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getMessagesFromServer();
        //messagesRecyclerView.setAdapter(new RecyclerViewAdapter(messages, ShowMessagesActivity.this, R.layout.message_row));

        Log.d(TAG, "size: " + messages.size());
    }

    public void getMessagesFromServer(){
        // prepare the Request
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse");
                        Log.d(TAG, response.toString());
                        try{

                            JSONArray jsonArray = new JSONArray(response);
                            Log.d(TAG, "treating the JSON response...");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //Log.d(TAG, "JSON #" + i);
                                JSONObject explrObject = jsonArray.getJSONObject(i);
                                //Log.d(TAG, explrObject.toString());
                                DataMsg m = new DataMsg(Integer.parseInt(explrObject.get("id").toString()),
                                        Integer.parseInt(explrObject.get("student_id").toString()), Double.valueOf(explrObject.get("gps_lat").toString()),
                                        Double.valueOf(explrObject.get("gps_long").toString()),
                                        explrObject.get("student_message").toString());
                                messages.add(m);
                                Log.d(TAG, "added message with id:" + m.getId());
                            }
                            Log.d(TAG, "size: " + messages.size());
                            messagesRecyclerView.setAdapter(new RecyclerViewAdapter(messages, ShowMessagesActivity.this, R.layout.message_row));
                            messagesRecyclerView.getAdapter().notifyDataSetChanged();
                        }catch(JSONException e){
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse");
                        Log.d(TAG, error.toString());
                    }
                }
        );
        if(getRequest == null){
            Log.d(TAG, "getRequest is null");
        }
        requestQueue.add(getRequest);

    }


}
