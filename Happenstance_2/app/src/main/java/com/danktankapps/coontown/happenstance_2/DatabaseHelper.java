package com.danktankapps.coontown.happenstance_2;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by coontown on 8/5/15.
 */
public class DatabaseHelper {

    ProgressDialog progDiag;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADDRESS = "http://happenstance.site40.net/";

    public DatabaseHelper(Context context){
        progDiag = new ProgressDialog(context);
        progDiag.setCancelable(false);
        progDiag.setTitle(("processing"));
        progDiag.setMessage("please wait...");
    }

    public void isInDatabase(String phoneNumber, CheckPhoneNumberCallback callback){
        //TODO: make sure the phone number is in the data base!
        progDiag.show();
        new CheckPhoneNumberInDatabase(phoneNumber, callback).execute();
    }


    public void updateAndRetrieveLocations(FleetMember fleetMember, GetLocationsCallback callback){
        new GetLocationsAsyncTask(fleetMember, callback).execute();
    }

    public void updateFleetMemberLocation(FleetMember fleetMember, GetFleetMemberCallback callback){
        updateFleetMember(fleetMember, callback);
    }

    public void updateFleetMember(FleetMember fleetMember, GetFleetMemberCallback callback){
        //TODO: update the database!
        //progDiag.show();
        new UpdateFleetMemberAsyncTask(fleetMember, callback).execute();
    }

    public void retrieveCoordinates(GetLocationsCallback callback){
        //TODO: get the coordinates from the database and put into a list.
        //ArrayList<LatLng> coordinates = new ArrayList<LatLng>();
        //coordinates.add(new LatLng(32.72, -117.12));
        //coordinates.add(new LatLng(32.75, -117.06));
        new GetLocationsAsyncTask(new FleetMember(), callback).execute();
    }

    private String serverRequest(String post_params, String url){
        URL uarel = null;
        HttpURLConnection con = null;
        StringBuffer response = new StringBuffer();
        try{
            uarel = new URL(url);
            con = (HttpURLConnection) uarel.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(post_params.getBytes());
            os.flush();
            os.close();
            int responseCode = con.getResponseCode();
            Log.i("Update success?!  ", "POST Response Code :: " + responseCode);

            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                //StringBuffer response = new StringBuffer();

                while((inputLine = in.readLine()) != null){
                    response.append(inputLine);
                }
                in.close();
                Log.i("RESPONSE: ", response.toString());
            }
            else {
                Log.i("FAIL: ", "POST request did not work.");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return response.toString();
    }



//class for a fleet member to update their location
    public class UpdateFleetMemberAsyncTask extends AsyncTask<Void, Void, Void> {

        FleetMember fleetMember;
        GetFleetMemberCallback callback;

        public UpdateFleetMemberAsyncTask(FleetMember fleetMember, GetFleetMemberCallback cllbck){
            this.fleetMember = fleetMember;
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
            String POST_PARAMS = "phoneNumber=" + fleetMember.getNumber()+
                    "&latitude=" + fleetMember.getLatitude() +
                    "&longitude=" + fleetMember.getLongitude() +
                    "&working=" + fleetMember.getWorking();
            String response = serverRequest(POST_PARAMS, SERVER_ADDRESS + "UpdateFleetMember.php");
            return null;
        }
    }

//class for a user to get the list of working locations
    public class GetLocationsAsyncTask extends AsyncTask<Void, Void, ArrayList<LatLng>> {

        FleetMember fleetMember;
        //ArrayList<LatLng> latsAndLongs;
        GetLocationsCallback callback;

        public GetLocationsAsyncTask(FleetMember fleetMember, GetLocationsCallback cllbck){
            this.fleetMember = fleetMember;
            this.callback = cllbck;
        }


        @Override
        protected void onPostExecute(ArrayList<LatLng> ltlg) {

            progDiag.dismiss();
            callback.done(ltlg);
            super.onPostExecute(ltlg);
        }

        @Override
        protected ArrayList<LatLng> doInBackground(Void... params) {
            //access the SERVER!
            String POST_PARAMS = "phoneNumber=" + fleetMember.getNumber()+
                    "&latitude=" + fleetMember.getLatitude() +
                    "&longitude=" + fleetMember.getLongitude() +
                    "&working=" + fleetMember.getWorking();
            ArrayList<LatLng> list = new ArrayList<LatLng>();
            String response = serverRequest(POST_PARAMS, SERVER_ADDRESS + "GetLocations.php");
            //TODO: Break down reponse into an array list of coordinates for the map
            try {
                JSONArray json = new JSONArray(response);
                String nextString;
                for(int i = 0; i < json.length(); i++){
                    JSONObject obj = json.getJSONObject(i);
                    list.add(new LatLng(Double.parseDouble(obj.getString("latitude")), Double.parseDouble(obj.getString("longitude"))));
                    Log.i("UPDATE LAT: ", obj.getString("latitude"));
                    Log.i("UPDATE LNG: ", obj.getString("longitude"));
                }
            }
            catch(Exception e){
                try{
                    Log.i("Trying JSON Object", "");
                    JSONObject json = new JSONObject(response);
                    list.add(new LatLng(Double.parseDouble(json.getString("latitude")), Double.parseDouble(json.getString("longitude"))));
                    Log.i("Json object: ", "latitude = " + json.getString("latitude")+ " longitude: " + json.getString("longitude"));
                }

                catch(Exception e2){
                    e.printStackTrace();
                    e2.printStackTrace();
                }
            }
            return list;
        }
    }


    public class CheckPhoneNumberInDatabase extends AsyncTask<Void, Void, Boolean> {

        //FleetMember fleetMember;
        String number;
        CheckPhoneNumberCallback callback;
        //Boolean is_fleet_member;

        public CheckPhoneNumberInDatabase(String number, CheckPhoneNumberCallback callback){
            this.number = number;
            this.callback = callback;
            //this.is_fleet_member = false;
        }


        @Override
        protected void onPostExecute(Boolean isFleetMember) {

            progDiag.dismiss();
            callback.done(isFleetMember);
            super.onPostExecute(isFleetMember);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //access the SERVER!
            String POST_PARAMS = "phoneNumber=" + number;
            Log.i("Phone number: ", number);
            String response = serverRequest(POST_PARAMS, SERVER_ADDRESS + "CheckPhoneNumber.php");
            //TODO: Break down reponse into an array list of coordinates for the map
            Log.i("CHECKING NUMBER", response);
            try {
                JSONObject json = new JSONObject(response);
                if (json.getString("phoneNumber").equals(number)){
                    return true;
                }
                //return false;
            }
            catch(Exception e){
                e.printStackTrace();
                //return false;
            }
            return false;
        }
    }









}
