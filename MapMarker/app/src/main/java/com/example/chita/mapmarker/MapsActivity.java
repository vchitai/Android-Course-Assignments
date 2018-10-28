package com.example.chita.mapmarker;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Pair;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mMarker = new ArrayList<>();
        mMarker.add(new LatLng(10.7626772,106.6803805));
        mMarker.add(new LatLng(10.7704872,106.6604109));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng khtn = mMarker.get(0);
        Marker khtnMaker = mMap.addMarker(new MarkerOptions().position(khtn).title("KHTN"));
        khtnMaker.setTag("*101#");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(khtn));  // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        LatLng home = mMarker.get(1);
        Marker homeMarker = mMap.addMarker(new MarkerOptions().position(home).title("HomeSweetHome"));
        homeMarker.setTag("*101#");

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String sdt = (String)marker.getTag();
                CallTheNumber(sdt);
                return false;
            }
        });
    }

    private void CallTheNumber(String number) {
        if (number != null) {
            String command = "tel:" + number;
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(command));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}
