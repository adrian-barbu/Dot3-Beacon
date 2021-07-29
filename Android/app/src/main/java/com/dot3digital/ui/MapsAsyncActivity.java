package com.dot3digital.ui;

/**
 * Created by arkady on 06/08/15.
 */

import com.dot3digital.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

// https://developers.google.com/maps/documentation/android/start
public class MapsAsyncActivity extends FragmentActivity
        implements OnMapReadyCallback
{
    protected Double geoLat;
    protected Double geoLong;
    protected Float geoRadius;
    protected String geofenceName;
    protected String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_async_layout);

        this.geoLat = getIntent().getExtras().getDouble("MapsAsyncActivity_geoLat");
        this.geoLong = getIntent().getExtras().getDouble("MapsAsyncActivity_geoLong");
        this.geoRadius = getIntent().getExtras().getFloat("MapsAsyncActivity_geoRadius");
        this.geofenceName = getIntent().getExtras().getString("MapsAsyncActivity_geofenceName");
        this.placeName = getIntent().getExtras().getString("MapsAsyncActivity_placeName");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker onto the location, and move the camera.
        LatLng latLng = new LatLng(this.geoLat, this.geoLong);
        // Test:
        //latLng = new LatLng(43.441524, -80.562737); // Waterloo, ON

        map.addMarker(new MarkerOptions().position(latLng).title(this.geofenceName + "; " + this.placeName)); //(this.placeName));

        // Zooming and moving camera
        if (latLng.latitude == 0 || latLng.longitude == 0) { // temp. condition to filter the fake demo zero location
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng)); //Moves the camera to users current longitude and latitude
        } else {
            //map.animateCamera(CameraUpdateFactory.zoomTo(14.0f)); // problem: it leaves the marker out of screen
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));//Animates camera and zooms to preferred state on the user's current location.
        }
    }
}

