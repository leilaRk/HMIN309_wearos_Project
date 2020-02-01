package com.example.joseph.hmin309_wear_leila;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class DataMsg implements Serializable{

    private int id;
    private int student_id;
    private double gps_lat;
    private double gps_long;
    private String message;



    public DataMsg(int student_id, double gps_lat, double gps_long, String message) {
        this.student_id = student_id;
        this.gps_lat = gps_lat;
        this.gps_long = gps_long;
        this.message = message;
    }

    public DataMsg(int student_id, String message) {
        this.student_id = student_id;
        this.message = message;
    }

    public DataMsg(int id, int student_id, double gps_lat, double gps_long, String message) {
        this.id = id;
        this.student_id = student_id;
        this.gps_lat = gps_lat;
        this.gps_long = gps_long;
        this.message = message;
    }



    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        try {
            json.put("student_id", student_id);
            json.put("gps_lat", gps_lat);
            json.put("gps_long", gps_long);
            json.put("student_message", message);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    public int getId() {
        return id;
    }

    public int getStd_id() {
        return student_id;
    }

    public double getGps_lat() {
        return gps_lat;
    }

    public double getGps_long() {
        return gps_long;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public void setGps_lat(double gps_lat) {
        this.gps_lat = gps_lat;
    }

    public void setGps_long(double gps_long) {
        this.gps_long = gps_long;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}