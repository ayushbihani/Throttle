package com.example.user.fullthrottle;

/**
 * Created by bihaniayush on 13/5/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;//package com.example.user.fullthrottle;

/**
 * Created by User on 4/18/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

public class EventAdapter2 extends RecyclerView.Adapter<EventAdapter2.MyViewHolder> {

    private Context mContext;
    private List<Event> eventList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView start,destn,time;

        public MyViewHolder(View view) {
            super(view);
            start = (TextView) view.findViewById(R.id.StartPoint);
            destn = (TextView) view.findViewById(R.id.Place);
            time = (TextView) view.findViewById(R.id.Date_time);
        }
    }


    public EventAdapter2(Context mContext, List<Event> EventList) {
        this.mContext = mContext;
        this.eventList = EventList;
    }
    public EventAdapter2(List<Event>eventlist)
    {
        this.eventList=eventlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_event_save, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Event Event = eventList.get(position);
        holder.start.setText("Start :"+Event.getStart());
        holder.destn.setText("Destination :"+Event.getDestn());
        holder.time.setText("Date :"+Event.getDate());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}