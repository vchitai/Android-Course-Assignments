package com.example.chita.mapvelocity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng oldLocation;
    private Marker oldMaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        drawVector();
        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                drawVector();
                h.postDelayed(this, 1000);
            }
        }, 1000); // 1 second delay (takes millis)

    }

    private void drawVector() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (locationManager == null) {

        }

        Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (location != null) {

            if (oldMaker != null) {
                oldMaker.remove();
                oldMaker = null;
            }
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

            float v = 0;
            if (oldLocation != null) {
                Location old = new Location("");
                old.setLatitude(oldLocation.latitude);
                old.setLongitude(oldLocation.longitude);
                if (old.distanceTo(location) == 0) {
                    v = 0;
                } else {
                    v = location.getSpeed();
                }
            }
            oldMaker = mMap.addMarker(new MarkerOptions().position(currentLocation).title(Float.toString(v) + " m/s"));
            oldMaker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));

            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            if (oldLocation == null) {

            } else {
                Log.d("ahihi", "oldlocation: " + oldLocation.latitude + "," + oldLocation.longitude);
                PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                options.add(oldLocation,currentLocation);
                mMap.addPolyline(options);

            }
            oldLocation = currentLocation;
        } else {
            Log.d("ahihi", "location null");
        }

    }
}
