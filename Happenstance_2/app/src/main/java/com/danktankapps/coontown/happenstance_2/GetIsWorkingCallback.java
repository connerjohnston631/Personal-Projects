package com.danktankapps.coontown.happenstance_2;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by coontown on 8/14/15.
 */


interface GetFleetMemberCallback{
    public abstract void done(FleetMember fleetMember);
}

interface GetLocationsCallback{
    public abstract void done(ArrayList<LatLng> latLng);
}

interface CheckPhoneNumberCallback{
    public abstract void done(Boolean is_fleet_member);
}




