package com.danktankapps.coontown.happenstance;

import android.provider.BaseColumns;

/**
 * Created by coontown on 7/28/15.
 */
public class TableData {

    public TableData(){

        //public static final String
    }

    public static abstract class TableContents implements BaseColumns{

        public static final String USER_NAME = "user_name";
        public static final String USER_PASS = "user_pass";
        public static final String USER_LATITUDE = "user_latitude";
        public static final String USER_LONGITUDE = "user_longitude";

        public static final String DATABASE_NAME = "Happenstance_App";
        public static final String TABLE_NAME = "fleet_members";

    }
}

