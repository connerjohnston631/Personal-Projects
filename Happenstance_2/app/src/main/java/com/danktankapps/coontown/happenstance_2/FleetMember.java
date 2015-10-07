package com.danktankapps.coontown.happenstance_2;

/**
 * Created by coontown on 8/14/15.
 */
public class FleetMember {
    private String number;
    private String latitude;
    private String longitude;
    private String working;
    private boolean is_working;

    //the fleet member is meant to be updated at different points. Use the set methods to construct the
    //fleet member instead
    public FleetMember(String number, Double latitude, Double longitude, boolean is_working){
        this.number = number;
        this.latitude = Double.toString(latitude);
        this.longitude = Double.toString(longitude);
        this.is_working = is_working;
        this.working = (is_working?"yes":"no");
    }

    public FleetMember(){
        this("1111111111", new Double(0.0), new Double(0.0), false);
    }

    public void setNumber(String number){
        this.number = number;
    }

    public void setLatitude(Double latitude){
        this.latitude = Double.toString(latitude);
    }

    public void setLongitude(Double longitude){
        this.longitude = Double.toString(longitude);
    }

    public String getNumber(){
        return this.number;
    }

    public String getLatitude(){
        return this.latitude;
    }

    public String getLongitude(){
        return this.longitude;
    }

    public void setIsWorking(boolean is_working){
        this.is_working = is_working;
        this.working = (is_working?"yes":"no");
    }

    public boolean getIsWorking(){
        return this.is_working;
    }

    public void setWorking(String working){
        this.is_working = working.toLowerCase().equals(new String("yes"));
        this.working = (this.is_working?"yes":"no"); //must be yes or no, not just any string.
    }

    public String getWorking(){
        return this.working;
    }



}
