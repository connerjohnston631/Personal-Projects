package com.danktankapps.coontown.happenstance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.telephony.TelephonyManager;


public class SecondActivity extends ActionBarActivity implements View.OnClickListener {

    EditText phone_number;

    Button get_phone_number;

    TelephonyManager  tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        phone_number = (EditText) findViewById(R.id.phone_number);
        get_phone_number = (Button) findViewById(R.id.get_phone_number);


    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.get_phone_number:
                phone_number.setText(tm.getLine1Number().toString());
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
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
