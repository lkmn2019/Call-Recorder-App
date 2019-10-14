package com.example.phonecallrecorder;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by SonuShaikh on 01-08-2019.
 */

public class DatabaseSingleton {
    public static SQLiteDatabase database;

    public static SQLiteDatabase getInstance(Context activity){
        if(database==null)
            database = new DatabaseHandler(activity).getWritableDatabase();
        return database;
    }
}
