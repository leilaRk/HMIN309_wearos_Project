package com.example.joseph.hmin309_wear_leila;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableRecyclerView;

public class RecyclerViewAdapter extends WearableRecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<DataMsg> messages;
    public Context context;
    private int listItemLayout;

    public RecyclerViewAdapter(List<DataMsg> messages, Context context, int listItemLayout) {
        this.messages = messages;
        this.context = context;
        this.listItemLayout = listItemLayout;
    }

    public RecyclerViewAdapter(List<DataMsg> messages, int listItemLayout) {
        this.messages = messages;
        this.listItemLayout = listItemLayout;
        Log.d("Adapter", "contructor");
        Log.d("Adapter", "size: " + messages.size());
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Adapter", "onCreateViewHolder ");
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view, context);
        //view.setMinimumHeight(parent.getHeight()/4);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d("Adapter", "onBindViewHolder ");
        holder.id.setText(messages.get(position).getId()+"");
        holder.student_id.setText(messages.get(position).getStd_id()+"");
        holder.message.setText(messages.get(position).getMessage());
        holder.gps_long.setText(Double.valueOf(messages.get(position).getGps_long()).toString());
        holder.gps_lat.setText(Double.valueOf(messages.get(position).getGps_lat()).toString());
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView id, student_id, message, gps_long, gps_lat;
        Context mContext;

        public ViewHolder(@NonNull View itemView, Context mctxt) {
            super(itemView);
            Log.d("Adapter", "View Holder constructor");
            itemView.setOnClickListener(this);
            this.id = itemView.findViewById(R.id.id);
            this.student_id = itemView.findViewById(R.id.student_id);
            Log.d("Adapter", this.student_id.getText().toString());
            this.message = itemView.findViewById(R.id.message);
            this.gps_long = itemView.findViewById(R.id.gps_long);
            this.gps_lat = itemView.findViewById(R.id.gps_lat);
            mContext = mctxt;
        }

        @Override
        public void onClick(View v) {
            Log.d("Adapter", "OnClick()");
            Log.d("Adapter", "onClick " + getLayoutPosition() + " " + student_id.getText().toString());
            int pos = getAdapterPosition();

            DataMsg m = new DataMsg(Integer.parseInt(id.getText().toString()), Integer.parseInt(student_id.getText().toString()), Double.valueOf(gps_long.getText().toString()),Double.valueOf(gps_lat.getText().toString()), message.getText().toString());
            Intent intent = new Intent(v.getContext(), ViewMessageActivity.class);
            intent.putExtra("message", m);
            v.getContext().startActivity(intent);
        }
    }


}
