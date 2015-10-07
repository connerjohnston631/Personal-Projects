package com.danktankapps.coontown.happenstance_2;

import android.content.Context;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    Button update_button, remove_location, zoom_out, zoom_in;
    LocationManager locMan;
    MyLocationListener myLocList;
    EditText status;
    String status_string = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(Values.getIsFleetMember()){
            locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            myLocList = new MyLocationListener();
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocList);
        }

        update_button = (Button) findViewById(R.id.update);
        remove_location=(Button) findViewById(R.id.remove_location);
        status = (EditText) findViewById(R.id.status_et);
        zoom_in = (Button) findViewById(R.id.zoom_in);
        zoom_out = (Button) findViewById(R.id.zoom_out);
        remove_location.setVisibility(View.GONE);
        //if( Values.getIsFleetMember()){
        //    remove_location.setVisibility(View.VISIBLE);
        //}
        //else {
           // remove_location.setVisibility(View.GONE);
        //}
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                status_string += "update button clicked\n";
                if(Values.getIsFleetMember()){
                    if(myLocList.myLocation == null){
                        //presentToast("GPS Location Failed");
                        status_string += "no GPS Location\n";
                        status.setText(status_string);
                        return;
                    }
                    else {
                        status_string += "Updating fleet member location\n";
                        status.setText(status_string);
                        Values.setLatLong(myLocList.getLatitude(), myLocList.getLongitude());
                        dbHelper.updateFleetMember(new FleetMember(Values.getPhoneNumber(), Values.getLatitude(), Values.getLongitude(), true), new GetFleetMemberCallback() {
                            @Override
                            public void done(FleetMember fleetMember) {
                                //DatabaseHelper dbHelper = DatabaseHelper(getApplicationContext());
                                status_string += "done updating fleet member in database";
                                updateLocations();
                                remove_location.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
                else{
                    updateLocations();
                }
            }
        });

        remove_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Values.getIsFleetMember()){
                    removeLocation();
                }
            }
        });

        zoom_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        zoom_out.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
    }

    private void removeLocation(){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        dbHelper.updateFleetMember(new FleetMember(Values.getPhoneNumber(), 0.0, 0.0, false), new GetFleetMemberCallback() {
            @Override
            public void done(FleetMember fleetMember) {
                updateLocations();
            }
        });
    }

    private void presentToast(String toastText){
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
    }

    private void updateLocations(){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        dbHelper.retrieveCoordinates(new GetLocationsCallback() {
            @Override
            public void done(ArrayList<LatLng> latLng) {
                Values.setCoordinatesList(latLng);
                status_string += "There are " + Integer.toString(latLng.size()) + " locations";
                status.setText(status_string);
                setUpMap();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mMap.clear();

        Pair<LatLng, LatLng> pair = getBounds(Values.getCoordinatesList());
        if (pair == null){
            presentToast("No Coordinates Retrieved");
            //if no window provided, open the map to San Diego
            LatLng ex_1 = new LatLng(32.72, -117.12);
            LatLng ex_2 = new LatLng(32.75, -117.06);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(ex_1, ex_2), 1000, 1000, 2));
        }
        else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(pair.first, pair.second), 1000, 1000, 2));
            for (int i = 0; i < Values.getCoordinatesList().size(); i++) {
                mMap.addMarker(new MarkerOptions().position(Values.getCoordinatesList().get(i)).title(""));
                Log.i("ADDING MARKER", Double.toString(Values.getCoordinatesList().get(i).latitude) + "  " + Double.toString(Values.getCoordinatesList().get(i).longitude));
            }
        }

        mMap.setMyLocationEnabled(true);


    }


    private Pair<LatLng, LatLng> getBounds(ArrayList<LatLng> latLng){
        //Returns the lowest latitude, higest latitude, lowest longitude, highest longitude
        if(latLng.size() == 0) {return null;}
        Double minLat = latLng.get(0).latitude;
        Double maxLat = latLng.get(0).latitude;
        Double minLng = latLng.get(0).longitude;
        Double maxLng = latLng.get(0).longitude;
        if(latLng.size() > 1) {
            for (int i = 0; i < latLng.size(); i++) {
                if (latLng.get(i).latitude < minLat){
                    minLat = latLng.get(i).latitude;
                }
                else if(latLng.get(i).latitude > maxLat){
                    maxLat = latLng.get(i).latitude;
                }

                if (latLng.get(i).longitude < minLng){
                   minLng = latLng.get(i).longitude;
                }
                else if(latLng.get(i).longitude > maxLng){
                   maxLng = latLng.get(i).longitude;
                }
            }
        }
        return new Pair<LatLng, LatLng>(new LatLng(minLat, minLng), new LatLng(maxLat, maxLng));
    }
}
