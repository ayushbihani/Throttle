package com.example.user.fullthrottle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class Joined_rides_Activtiy extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences file;
    ArrayList<Event> events;
    EventAdapter2 adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_rides__activtiy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("FullThrottle");
        file=getApplicationContext().getSharedPreferences("JoinedRides",MODE_PRIVATE);
        editor=file.edit();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view_join);
        setStuff();
        Map<String,?> keys = file.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",entry.getKey() + ": " +
                    entry.getValue());
            Gson g = new Gson();
            String key = file.getString(entry.getKey(),"");
            Event e = g.fromJson(key,Event.class);
            events.add(e);
        }
        adapter.notifyDataSetChanged();


    }
    public void setStuff()
    {
        events = new ArrayList<>();
        adapter = new EventAdapter2(events);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


}
