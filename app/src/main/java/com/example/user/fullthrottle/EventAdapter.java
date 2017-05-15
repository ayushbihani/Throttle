package com.example.user.fullthrottle;

/**
 * Created by User on 4/18/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context mContext;
    private List<Event> eventList;
    private Button buttonJoin;
    private SharedPreferences joinedRide;
    private SharedPreferences.Editor editor;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView start,destn,time;

        public MyViewHolder(View view) {
            super(view);
            start = (TextView) view.findViewById(R.id.StartPoint);
            destn = (TextView) view.findViewById(R.id.Place);
            time = (TextView) view.findViewById(R.id.Date_time);
            buttonJoin = (Button)view.findViewById(R.id.joinButton);
        }
    }


    public EventAdapter(Context mContext, List<Event> EventList) {
        this.mContext = mContext;
        this.eventList = EventList;
        joinedRide = mContext.getSharedPreferences("JoinedRides",Context.MODE_PRIVATE);
        editor= joinedRide.edit();
    }
    public EventAdapter(List<Event>eventlist)
    {
        this.eventList=eventlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_event, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Event Event = eventList.get(position);
        holder.start.setText("Start :"+Event.getStart());
        holder.destn.setText("Destination :"+Event.getDestn());
        holder.time.setText("Date :"+Event.getDate());
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtopreference(Event.getStart(),Event.getDestn(),Event.getDate());
                Toast.makeText(mContext,"hello",Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext,"why",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void addtopreference(String start,String destn,String date)
    {
        Event e = new Event(date,start,destn);
        Gson gson=new Gson();
        String json=gson.toJson(e);
        editor.putString(start.concat(destn),json);
        editor.apply();
        editor.commit();
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}