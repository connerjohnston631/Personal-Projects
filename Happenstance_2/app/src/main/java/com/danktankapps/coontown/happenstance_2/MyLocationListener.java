package com.danktankapps.coontown.happenstance_2;

import android.location.LocationListener;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by coontown on 8/6/15.
 */


public class MyLocationListener implements LocationListener {

    public Location myLocation;

    public Double getLatitude(){return myLocation.getLatitude();}

    public Double getLongitude(){return myLocation.getLongitude();}

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        location.getLatitude();
        location.getLongitude();

        String myLocation = "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();

        //I make a log to see the results
        Log.e("MY CURRENT LOCATION", myLocation);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}