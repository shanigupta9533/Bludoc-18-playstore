package com.likesby.bludoc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Bludoc.db";
    private static final int DATABASE_VERSION = 5;

    private static final String CREATE_MEDICINES =
            "create table  tbl_medicine( medicine_id	TEXT, medicine_name TEXT, frequency TEXT, no_of_days TEXT, route	TEXT, instruction TEXT,additional_comment TEXT," +
                    "created_by	TEXT, created TEXT, modified TEXT, qty TEXT)";

    private static final String CREATE_LABTEST =
            "create table  tbl_labtest( lab_id TEXT,lab_test_name TEXT,lab_test_comment TEXT)";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_MEDICINES);
        database.execSQL(CREATE_LABTEST);
       // database.execSQL(CREATE_LOCATION);
       // database.execSQL(CREATE_TRACKING);
       // database.execSQL(CREATE_CONTACTS);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.e(MyDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS MyEmployees");
        database.execSQL("DROP TABLE IF EXISTS tbl_medicine");
        database.execSQL("DROP TABLE IF EXISTS tbl_labtest");
        onCreate(database);
    }

}
