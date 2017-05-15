package com.example.user.fullthrottle;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by bihaniayush on 15/5/17.
 */

public class MapsActivity  extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Double startlat,destnlat,startlong,destnlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        startlat = Double.parseDouble(getIntent().getExtras().getString("startlat"));
        startlong = Double.parseDouble(getIntent().getExtras().getString("startlong"));
        destnlat= Double.parseDouble(getIntent().getExtras().getString("destlat"));
        destnlong = Double.parseDouble(getIntent().getExtras().getString("destlong"));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<Marker> markers = new ArrayList<>();
        // Add a marker in Sydney and move the camera
        LatLng start = new LatLng(startlat,startlong);
        LatLng end = new LatLng(destnlat,destnlong);
        Marker marker1 =mMap.addMarker(new MarkerOptions().position(start));
        Marker marker2 =mMap.addMarker(new MarkerOptions().position(end));
        markers.add(marker1);
        markers.add(marker2);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
// end of new code

        mMap.animateCamera(cu);

    }
}
