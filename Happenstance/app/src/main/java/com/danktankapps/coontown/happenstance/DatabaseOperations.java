package com.danktankapps.coontown.happenstance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by coontown on 7/28/15.
 */
public class DatabaseOperations extends SQLiteOpenHelper {

    public static final int database_version = 1;
    public static final String USER_PHONE = "phone_number_hashed";
    public static final String USER_LATITUDE = "user_latitude";
    public static final String USER_LONGITUDE = "user_longitude";

    public static final String DATABASE_NAME = "Happenstance_App";
    public static final String TABLE_NAME = "fleet_members";


    public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" +
            USER_PHONE + " TEXT," + USER_LATITUDE + " TEXT," + USER_LONGITUDE + " TEXT );";

    public DatabaseOperations(Context context){
        super(context, DATABASE_NAME, null, database_version );
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        try{
            db.execSQL(CREATE_QUERY);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2){


    }
}
