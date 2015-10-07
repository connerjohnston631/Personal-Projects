package com.danktankapps.coontown.mysqlpractice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends Activity {

    Button loginButton;
    EditText usernameEt, passwordEt;
    TextView registerLink;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.buttonLogin);
        usernameEt = (EditText) findViewById(R.id.etUsername);
        passwordEt = (EditText) findViewById(R.id.etPassword);
        registerLink = (TextView) findViewById(R.id.registerLink);

        userLocalStore = new UserLocalStore(this);

        //try {
         //   URL url = new URL("http://happenstance.site40.net/Login.php");
          //  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //}
        //catch(Exception e){e.printStackTrace();}

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEt.getText().toString();
                String password = passwordEt.getText().toString();

                User user = new User(username, password);

                authenticate(user);

                userLocalStore.storeUserData(user);
                userLocalStore.setUserLoggedIn(true);

                //startActivity(new Intent(Login.this, Logout.class));
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        
    }


    private void authenticate(User user){
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if(returnedUser == null){
                    showErrorMessage();
                }
                else{
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void logUserIn(User returnedUser){
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        Log.i("User data:", returnedUser.name + Integer.toString(returnedUser.age)+ returnedUser.password + returnedUser.username);
        startActivity(new Intent(Login.this, Logout.class));

    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("incorrect user detail");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
  /*  @Override
    protected void onStart(){
        super.onStart();
        if(authenticate() == true){

            displayUserDetails();
        }


    }

    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();

        nameE
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }*/



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
