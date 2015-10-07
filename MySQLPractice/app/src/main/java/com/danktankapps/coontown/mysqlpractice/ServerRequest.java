package com.danktankapps.coontown.mysqlpractice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by coontown on 8/11/15.
 */
public class ServerRequest {

    ProgressDialog progDiag;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADDRESS = "http://happenstance.site40.net/";


    public ServerRequest(Context context){
        progDiag = new ProgressDialog(context);
        progDiag.setCancelable(false);
        progDiag.setTitle(("processing"));
        progDiag.setMessage("please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback callback){
        progDiag.show();
        new StoreUserDataAsyncTask(user, callback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback callback){

        progDiag.show();
        new FetchUserDataAsyncTask(user, callback).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void>{

        User user;
        GetUserCallback callback;

        public StoreUserDataAsyncTask(User usr, GetUserCallback cllbck){
            this.user = usr;
            this.callback = cllbck;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            progDiag.dismiss();
            callback.done(null);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            //access the SERVER!
            String POST_PARAMS = "name=" + user.name +
                                "&age=" + Integer.toString(user.age) +
                                "&username=" + user.username +
                                "&password=" + user.password;


            URL url = null;
            HttpURLConnection con = null;

            try{
                url = new URL(SERVER_ADDRESS + "Register.php");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(POST_PARAMS.getBytes());
                os.flush();
                os.close();
                int responseCode = con.getResponseCode();
                Log.i("Register success?!  ", "POST Response Code :: " + responseCode);

                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while((inputLine = in.readLine()) != null){
                        response.append(inputLine);
                    }
                    in.close();
                    Log.i("REGISTRATION RESPONSE: ", response.toString());
                }
                else {
                    Log.i("FAIL: ", "POST request did not work.");
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }







    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User>{

        User user;
        GetUserCallback callback;

        public FetchUserDataAsyncTask(User usr, GetUserCallback cllbck){
            this.user = usr;
            this.callback = cllbck;
        }


        @Override
        protected void onPostExecute(User returnedUser) {

            progDiag.dismiss();
            callback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }

        @Override
        protected User doInBackground(Void... params) {
            //access the SERVER!
            String POST_PARAMS = "username=" + user.username +
                    "&password=" + user.password;


            URL url = null;
            HttpURLConnection con = null;
            User user = new User("name", 1, "username", "password");

            try{
                url = new URL(SERVER_ADDRESS + "Login.php");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(POST_PARAMS.getBytes());
                os.flush();
                os.close();
                int responseCode = con.getResponseCode();
                Log.i("Register success?!  ", "POST Response Code :: " + responseCode);

                StringBuffer response = new StringBuffer();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    //StringBuffer response = new StringBuffer();

                    while((inputLine = in.readLine()) != null){
                        response.append(inputLine);
                    }
                    in.close();
                    Log.i("Login RESPONSE: ", response.toString());
                }
                else {
                    Log.i("FAIL: ", "POST request did not work.");
                }
                JSONObject json = new JSONObject(response.toString());
                user.name = json.getString("name");
                user.age = (int) json.get("age");
                user.username = json.getString("username");
                user.password = json.getString("password");
                //user.age = (int) json.get("age");

            }
            catch (Exception e){
                e.printStackTrace();
            }

            return user;
        }
    }


}
