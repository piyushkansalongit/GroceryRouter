package com.example.groceryrouter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DeliveryCoordinatesDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "deliveryLocationsDB";
    private static final String TABLE_NAME = "deliveryDetails";
    private static final String COL1 = "ID";
    private static final String COL2 = "Latitude";
    private static final String COL3 = "Longitude";
    private static final String COL4 = "Demand";
    private static final String COL5 = "Label";

    DeliveryCoordinatesDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null ,3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + " Latitude TEXT, Longitude TEXT, Demand TEXT, Label TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    boolean addData(String Latitude, String Longitude, String Demand, String Label)
    {
        SQLiteDatabase db = WelcomeActivity.deliveryCoordinatesDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, Latitude);
        contentValues.put(COL3, Longitude);
        contentValues.put(COL4, Demand);
        contentValues.put(COL5, Label);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;

    }
    boolean updateData(String ID, String Latitude, String Longitude, String Demand, String Label)
    {
        SQLiteDatabase db = WelcomeActivity.deliveryCoordinatesDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, ID);
        contentValues.put(COL2, Latitude);
        contentValues.put(COL3, Longitude);
        contentValues.put(COL4, Demand);
        contentValues.put(COL5, Label);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{ID});
        return true;
    }

    Integer deleteData(String ID)
    {
        SQLiteDatabase db = WelcomeActivity.deliveryCoordinatesDB.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{ID});
    }
    Cursor showData(){
        SQLiteDatabase db = WelcomeActivity.deliveryCoordinatesDB.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        return data;
    }

    void deleteAll(){
        SQLiteDatabase db = WelcomeActivity.deliveryCoordinatesDB.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
