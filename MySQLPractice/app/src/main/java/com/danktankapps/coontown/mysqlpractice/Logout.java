package com.danktankapps.coontown.mysqlpractice;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Logout extends Activity {

    Button logoutButton;
    EditText nameET, usernameET, ageET;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        logoutButton = (Button) findViewById(R.id.buttonLogout);
        nameET = (EditText) findViewById(R.id.etNameLO);
        usernameET = (EditText) findViewById(R.id.etUsernameLO);
        ageET = (EditText) findViewById(R.id.etAgeLO);

        userLocalStore = new UserLocalStore(this);



        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(Logout.this, Login.class));
            }
        });

    }


    @Override
    protected void onStart(){

        super.onStart();
        if(authenticate()){
            displayUserDetails();
        }
        else{
            startActivity(new Intent(Logout.this, Login.class));
        }
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();
        nameET.setText(user.name);
        usernameET.setText(user.username);
        ageET.setText(Integer.toString(user.age));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
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
