package com.example.joseph.hmin309_wear_leila;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class Send_Activity extends WearableActivity {
    public EditText stdId, stdMessage;
    private FusedLocationProviderClient mFusedLocationClient;
    String TAG = "Send_Activity";
    String url ="https://hmin309-embedded-systems.herokuapp.com/message-exchange/messages/";
    RequestQueue queue;
    LinearLayout linearLayout, ambientLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_);
        this.stdId = findViewById(R.id.stdId);
        this.stdMessage = findViewById(R.id.stdMessage);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        linearLayout = findViewById(R.id.sendLayout);
        ambientLayout = findViewById(R.id.sendAmbientLayout);

        Log.d(TAG, "On create");
        setAmbientEnabled();
        queue = Volley.newRequestQueue(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ){//Can add more as per requirement

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        123);
            }
        }
    }

    public void sendMessageToServer(View view){
        mFusedLocationClient.getLastLocation()
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "On failure");
                        Log.d(TAG, e.getMessage());
                        Toast.makeText(Send_Activity.this, "", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations, this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            double gpslong_round, gpslat_round;
                            gpslong_round = (double) (Math.round( location.getLongitude() * 1000))/1000;
                            gpslat_round = (double) (Math.round( location.getLatitude() * 1000))/1000;

                            Log.d(TAG, "location: " + location.getAltitude() + "--" + location.getLongitude());
                            final DataMsg message = new DataMsg(Integer.parseInt(stdId.getText().toString()), gpslong_round, gpslat_round, stdMessage.getText().toString());
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d(TAG, "onresponse: " + response.toString());
                                            Toast.makeText(Send_Activity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Send_Activity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d(TAG, "onError: " + error.networkResponse.allHeaders.toString());
                                        }
                                    }
                            ){
                                @Override
                                public String getBodyContentType(){
                                    return "application/json; charset=utf-8";
                                }

                                public byte[] getBody() throws AuthFailureError {
                                    Log.d(TAG, "getBody");
                                    try {
                                        Log.d(TAG, "getBody: messagetopass: " + message.toJSON().toString());
                                        return message.toJSON().toString() == null ? null : message.toJSON().toString().getBytes("utf-8");
                                    } catch (UnsupportedEncodingException uee) {
                                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", message.toJSON().toString(), "utf-8");
                                        return null;
                                    }
                                }

                                @Override
                                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                    Log.d(TAG, "parseNetworkResponse");
                                    String responseString = "";
                                    if (response != null) {
                                        responseString = String.valueOf(response.statusCode);
                                        // can get more details such as response.headers
                                        Log.d(TAG, response.toString());
                                    }
                                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                                }
                            };

                            queue.add(stringRequest);

                        }
                        else{
                            Log.d(TAG, "location is null");
                        }
                    }
                });
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        linearLayout.setVisibility(View.GONE);
        ambientLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        linearLayout.setVisibility(View.VISIBLE);
        ambientLayout.setVisibility(View.GONE);
    }

}
