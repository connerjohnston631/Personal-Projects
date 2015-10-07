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

public class Register extends Activity {

    Button registerButton;
    EditText nameET, usernameET, ageET, passwordET;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = (Button) findViewById(R.id.buttonRegister);
        nameET = (EditText) findViewById(R.id.etNameReg);
        usernameET = (EditText) findViewById(R.id.etUsernameReg);
        passwordET = (EditText) findViewById(R.id.etPasswordReg);
        ageET = (EditText) findViewById(R.id.etAgeReg);

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                int age = Integer.parseInt(ageET.getText().toString());
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();

                User user = new User(name, age, username, password);

                registerUser(user);


                //startActivity(new Intent(Register.this, Login.class));
            }
        });

    }

    private void registerUser(User user){
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
