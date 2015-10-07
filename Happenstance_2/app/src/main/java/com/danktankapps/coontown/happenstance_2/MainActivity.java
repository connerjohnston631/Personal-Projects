package com.danktankapps.coontown.happenstance_2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends Activity {

    EditText status;
    String status_string;
    TextView facebook_tv;
    TextView map_tv;
    TextView instagram_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Auto generated steps
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Link the contents of the screen for manipulation
        status = (EditText) findViewById(R.id.status_et);
        facebook_tv = (TextView) findViewById(R.id.facebook_tv);
        map_tv = (TextView) findViewById(R.id.map_tv);
        instagram_tv = (TextView) findViewById(R.id.instagram_tv);

        map_tv.setVisibility(View.GONE);
        //Check the phone number in the database
        status_string = new String("Checking phone number");
        status.setText(status_string);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Values.setPhoneNumber(tm.getLine1Number());
        status_string += (" " + Values.getPhoneNumber() + "\n");
        status.setText(status_string);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.isInDatabase(Values.getPhoneNumber(), new CheckPhoneNumberCallback() {
            @Override
            public void done(Boolean in_database) {
                postCheckPhoneNumber(in_database);
            }
        });

        facebook_tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                goToWebsite("https://www.facebook.com/ilovehappenstance");

            }
        });

        instagram_tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                goToWebsite("https://instagram.com/ilovehappenstance/");
            }
        });

        map_tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });


    }

    private void goToWebsite(String string){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(string));
        startActivity(browserIntent);
    }

    //This is done once the phone number is checked. Locations of active fleet members is obtained.
    public void postCheckPhoneNumber(boolean is_in_database){
        status_string += "You are " + (is_in_database?" ":"not ") + "a Fleet Member in our databases.";
        status.setText(status_string);
        if(is_in_database){
            Values.setIsFleetMember(true);
        }
        else{
            Values.setIsFleetMember(false);
        }
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.retrieveCoordinates(new GetLocationsCallback(){
            @Override
            public void done(ArrayList<LatLng> latLng){
                Values.setCoordinatesList(latLng);
                map_tv.setVisibility(View.VISIBLE);
                //startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

    }


    //----------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }
}
