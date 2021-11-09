package com.likesby.bludoc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.likesby.bludoc.ModelLayer.Entities.LabTestItem;
import com.likesby.bludoc.ModelLayer.Entities.MedicinesItem;
import com.likesby.bludoc.ModelLayer.Entities.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyDB {

    private SQLiteDatabase database;

    //TABLW => tbl_medicine
    private final static String Tbl_medicine = "tbl_medicine";
    private final static String Medicine_id = "medicine_id"; // <= PK
    private final static String Medicine_name = "medicine_name";
    private final static String Frequency = "frequency";
    private final static String No_of_days = "no_of_days";
    private final static String Route = "route";
    private final static String Instruction = "instruction";
    private final static String Additional_comment = "additional_comment";
    private final static String Created_by = "created_by";
    private final static String Created = "created";
    private final static String Modified = "modified";
    private final static String qty = "qty";


    //TABLW => tbl_labtest
    private final static String Tbl_labtest = "tbl_labtest";
    private final static String Lab_id = "lab_id"; // <= PK
    private final static String Lab_test_name = "lab_test_name";
    private final static String Lab_test_comment = "lab_test_comment";

    //TABLW =>  tbl_user_sub
    private final static String Tbl_user_sub = "tbl_user_sub";
    private final static String Customers_subscription_id = "customers_subscription_id"; // <= PK
    private final static String Subscription_id = "subscription_id"; // <= PK
    private final static String Category_id = "category_id";
    private final static String CategoryName = "category_name";
    private final static String Offer_id = "offer_id";
    private final static String Start_date = "start_date";
    private final static String End_date = "end_date";
    private final static String Days = "days";
    private final static String No_person = "no_person";
    private final static String Subscription_type = "subscription_type";
    private final static String Amount = "amount";
    private final static String Offer_amount = "offer_amount";
    private final static String Final_amount = "final_amount";
    private final static String Payment_status = "payment_status";
    // private final static String Created = "created";
    // private final static String Modified = "modified";


    //TABLW => tbl_user
    private final static String Tbl_location = "tbl_location";
    private final static String Lat = "lat"; // <= PK
    private final static String Lng = "lng";
    private final static String LocationName = "location_name";

    //TABLW => tbl_user
    private final static String Tbl_tracking = "tbl_tracking";
    private final static String Tracking_id = "tracking_id"; // <= PK

    //TABLW => tbl_user
    private final static String Tbl_contacts = "tbl_contacts";
    private final static String Contact_id = "contact_id";
    private final static String Contact_name = "contact_name";
    private final static String Contact_mobile = "contact_mobile";


    /**
     * @param context
     */
    public MyDB(Context context) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
    }

    public boolean deleteAllMedicines() {
        //SQLiteDatabase db = this.getWritableDatabase();
        try {
            database.execSQL("DELETE FROM " + Tbl_medicine);
            return true;
        } catch (SQLiteException e) {
            return false;
        }

    }

    public boolean deleteAllLabTests() {
        //SQLiteDatabase db = this.getWritableDatabase();
        try {
            database.execSQL("DELETE FROM " + Tbl_labtest);
            return true;
        } catch (SQLiteException e) {
            return false;
        }

    }


    public boolean deleteUserSub() {
        //SQLiteDatabase db = this.getWritableDatabase();
        try {
            database.execSQL("DELETE FROM " + Tbl_user_sub);
            return true;
        } catch (SQLiteException e) {
            return false;
        }

    }

    public ArrayList<LabTestItem> getSearchDataLab(String searchData) {
        ArrayList<LabTestItem> labTestsAL = new ArrayList<>();

        //SQLiteDatabase sqLiteDatabase = null;
        try {
            String query = "Select * from " + Tbl_labtest + " where lab_test_name like '%%" + searchData + "'";
            Cursor cursor = database.rawQuery(query, null);
            while (cursor.moveToNext()) {
                // Log.e(TAG,"getAllVideos ________________________ videoListDB = "+cursor.getString(cursor.getColumnIndex("video_name")));

                LabTestItem labTestItem = new LabTestItem();


                labTestItem.setLabId(cursor.getString(cursor.getColumnIndex(Lab_id)));
                labTestItem.setLabTestName(cursor.getString(cursor.getColumnIndex(Lab_test_name)));
                labTestItem.setLabTestComment(cursor.getString(cursor.getColumnIndex(Lab_test_comment)));


                labTestsAL.add(labTestItem);
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error in **************************** getting Medicines " + ex.toString());
            return labTestsAL;
        }
        return labTestsAL;
    }


    public ArrayList<MedicinesItem> getSearchData(String searchData) {
        ArrayList<MedicinesItem> medicinesAL = new ArrayList<>();

        //SQLiteDatabase sqLiteDatabase = null;
        try {
            String query = "Select * from " + Tbl_medicine + " where medicine_name like '%%" + searchData + "'";
            Cursor cursor = database.rawQuery(query, null);
            while (cursor.moveToNext()) {
                // Log.e(TAG,"getAllVideos ________________________ videoListDB = "+cursor.getString(cursor.getColumnIndex("video_name")));

                MedicinesItem medicinesItem = new MedicinesItem();
                medicinesItem.setMedicineId(cursor.getString(cursor.getColumnIndex(Medicine_id)));
                medicinesItem.setMedicineName(cursor.getString(cursor.getColumnIndex(Medicine_name)));
                medicinesItem.setFrequency(cursor.getString(cursor.getColumnIndex(Frequency)));
                medicinesItem.setNoOfDays(cursor.getString(cursor.getColumnIndex(No_of_days)));
                medicinesItem.setRoute(cursor.getString(cursor.getColumnIndex(Route)));
                medicinesItem.setInstruction(cursor.getString(cursor.getColumnIndex(Instruction)));
                medicinesItem.setCreatedBy(cursor.getString(cursor.getColumnIndex(Created_by)));
                medicinesItem.setAdditionaComment(cursor.getString(cursor.getColumnIndex(Additional_comment)));
                medicinesItem.setCreated(cursor.getString(cursor.getColumnIndex(Created)));
                medicinesItem.setModified(cursor.getString(cursor.getColumnIndex(Modified)));
                medicinesItem.setQty(cursor.getString(cursor.getColumnIndex(qty)));

                medicinesAL.add(medicinesItem);
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error in **************************** getting Medicines " + ex.toString());
            return medicinesAL;
        }
        return medicinesAL;
    }


    public long createRecordsMedicine(ArrayList<MedicinesItem> medicinesItemArrayList) {
        long l = 0;

        for (MedicinesItem medicinesItem : medicinesItemArrayList
        ) {

            int x = 0;
            String query1 = "select count(*) from " + Tbl_medicine + " where medicine_id = '" + medicinesItem.getMedicineId() + "'";
            Cursor cursor = database.rawQuery(query1, null);
            if (cursor.moveToFirst()) {
                do {
                    //checkExist.set(0, cursor1.getString(0));
                    if (cursor.getInt(0) > 0) {
                        x = 1;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();

            if (x == 0) {
                database.beginTransaction();
                ContentValues values = new ContentValues();
                values.put(Medicine_id, medicinesItem.getMedicineId());
                values.put(Medicine_name, medicinesItem.getMedicineName());
                values.put(Frequency, medicinesItem.getFrequency());
                values.put(No_of_days, medicinesItem.getNoOfDays());
                values.put(Route, medicinesItem.getRoute());
                values.put(Instruction, medicinesItem.getInstruction());
                values.put(Additional_comment, medicinesItem.getAdditionaComment());
                values.put(Created_by, medicinesItem.getCreatedBy());
                values.put(Created, medicinesItem.getCreated());
                values.put(Modified, medicinesItem.getModified());
                values.put(qty, medicinesItem.getQty());


                try {
                    l = database.insert(Tbl_medicine, null, values);
                } catch (Exception e) {
                    Log.e(TAG, "Create_Records_Medicine SQLException = " + e);

                }


                database.setTransactionSuccessful();
                database.endTransaction();
            }
        }
        return l;
    }

    public long createRecordsLabTest(ArrayList<LabTestItem> labTestItemArrayList) {
        long l = 0;
        for (LabTestItem labTestItem : labTestItemArrayList
        ) {

            ContentValues values = new ContentValues();
            values.put(Lab_id, labTestItem.getLabId());
            values.put(Lab_test_name, labTestItem.getLabTestName());
            values.put(Lab_test_comment, labTestItem.getLabTestComment());

            try {
                l = database.insert(Tbl_labtest, null, values);
            } catch (Exception e) {
                Log.e(TAG, "Create_Records_LabTests SQLException = " + e);

            }
        }
        return l;
    }


    public ArrayList<MedicinesItem> getMedicines() {
        ArrayList<MedicinesItem> medicinesAL = new ArrayList<>();

        //SQLiteDatabase sqLiteDatabase = null;
        try {
            String query = "Select * from " + Tbl_medicine;
            Cursor cursor = database.rawQuery(query, null);
            while (cursor.moveToNext()) {
                // Log.e(TAG,"getAllVideos ________________________ videoListDB = "+cursor.getString(cursor.getColumnIndex("video_name")));

                MedicinesItem medicinesItem = new MedicinesItem();


                medicinesItem.setMedicineId(cursor.getString(cursor.getColumnIndex(Medicine_id)));
                medicinesItem.setMedicineName(cursor.getString(cursor.getColumnIndex(Medicine_name)));
                medicinesItem.setFrequency(cursor.getString(cursor.getColumnIndex(Frequency)));
                medicinesItem.setNoOfDays(cursor.getString(cursor.getColumnIndex(No_of_days)));
                medicinesItem.setRoute(cursor.getString(cursor.getColumnIndex(Route)));
                medicinesItem.setInstruction(cursor.getString(cursor.getColumnIndex(Instruction)));
                medicinesItem.setCreatedBy(cursor.getString(cursor.getColumnIndex(Created_by)));
                medicinesItem.setAdditionaComment(cursor.getString(cursor.getColumnIndex(Additional_comment)));
                medicinesItem.setCreated(cursor.getString(cursor.getColumnIndex(Created)));
                medicinesItem.setModified(cursor.getString(cursor.getColumnIndex(Modified)));
                medicinesItem.setQty(cursor.getString(cursor.getColumnIndex(qty)));

                medicinesAL.add(medicinesItem);
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error in **************************** getting Medicines " + ex.toString());
            return medicinesAL;
        }
        return medicinesAL;
    }


    public ArrayList<LabTestItem> getLabTests() {
        ArrayList<LabTestItem> labTestsAL = new ArrayList<>();

        //SQLiteDatabase sqLiteDatabase = null;
        try {
            String query = "Select * from " + Tbl_labtest;
            Cursor cursor = database.rawQuery(query, null);
            while (cursor.moveToNext()) {
                // Log.e(TAG,"getAllVideos ________________________ videoListDB = "+cursor.getString(cursor.getColumnIndex("video_name")));

                LabTestItem labTestItem = new LabTestItem();


                labTestItem.setLabId(cursor.getString(cursor.getColumnIndex(Lab_id)));
                labTestItem.setLabTestName(cursor.getString(cursor.getColumnIndex(Lab_test_name)));
                labTestItem.setLabTestComment(cursor.getString(cursor.getColumnIndex(Lab_test_comment)));


                labTestsAL.add(labTestItem);
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error in **************************** getting Medicines " + ex.toString());
            return labTestsAL;
        }
        return labTestsAL;
    }


}
