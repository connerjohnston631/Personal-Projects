package com.danktankapps.coontown.happenstance_2;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coontown on 8/6/15.
 */
public class Values {
    private static String my_phone_number;
    private static Double my_latitude;
    private static Double my_longitude;
    private static boolean is_fleet_member = false;
    private static ArrayList<LatLng> coordinatesList;


    public static void setPhoneNumber(String phoneNumber){
        my_phone_number = phoneNumber;
    }

    public static void setLatLong(Double latitude, Double longitude){
        my_latitude = latitude;
        my_longitude = longitude;
    }

    public static void setIsFleetMember(boolean is_flt_mem){
        is_fleet_member = is_flt_mem;
    }

    public static String getPhoneNumber(){
        return my_phone_number;
    }

    public static Double getLatitude(){
        return my_latitude;
    }

    public static Double getLongitude(){
        return my_longitude;
    }

    public static boolean getIsFleetMember(){
        return is_fleet_member;
    }

    public static ArrayList<LatLng> getCoordinatesList(){
        return coordinatesList;
    }

    public static void setCoordinatesList(ArrayList<LatLng> coordList) {
        coordinatesList = coordList;
    }





}
