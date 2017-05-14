package com.example.user.fullthrottle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import static android.text.TextUtils.isEmpty;

/**
 * Created by User on 4/10/2017.
 */

public class EventActivity extends AppCompatActivity{
    private FirebaseAuth auth;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private DatabaseReference dref;
    private EventAdapter adapter;
    private ArrayList<Event> events;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return  true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setContentView(R.layout.navigation_event);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        setStuff();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EventActivity.this,RideInputActivity.class);
                startActivity(i);
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(EventActivity.this,new RecyclerTouchListener.OnItemClickListener()
        {
            public void onItemClick(View view, int position)
            {
                Event e = events.get(position);
                Toast.makeText(EventActivity.this,e.getStart(),Toast.LENGTH_SHORT).show();
            }
        }));
        dref = FirebaseDatabase.getInstance().getReference().child("event");
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> data=dataSnapshot.getChildren().iterator();
                while(data.hasNext())
                {
                    Iterator<DataSnapshot> d= data.next().getChildren().iterator();
                    while(d.hasNext())
                    {
                        DataSnapshot snap = d.next();
                        Event e = snap.getValue(Event.class);
                       // if(!isEmpty(e.destination) ||  !(e.getStart() ==null))
                        events.add(e);
                    }

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setStuff()
    {
        mPlanetTitles = new String[2];
        mPlanetTitles[0]="Your rides";
        mPlanetTitles[1]="Joined Rides";
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        events = new ArrayList<Event>();
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        adapter = new EventAdapter(getApplicationContext(),events);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    public boolean checksession()
    {
        if(FirebaseAuth.getInstance().getCurrentUser() ==null)
        {
            return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.logout)
        {


            auth.signOut();
            boolean sess=checksession();
            if(sess)
            {
                Intent i =new Intent(EventActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
            else
                Toast.makeText(EventActivity.this,"Error in logging out",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();


    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on positi
        // on
        Toast.makeText(EventActivity.this,"Hello",Toast.LENGTH_SHORT).show();
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);

        switch (position)
        {
            case 0:
                    startActivity( new Intent(EventActivity.this,Your_rides_activity.class));
                    break;
            case 1:
                startActivity( new Intent(EventActivity.this,Joined_rides_Activtiy.class));
                break;
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}

