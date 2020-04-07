package com.example.groceryrouter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DeliveryAgentsDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "deliveryAgentsDB";
    public static final String TABLE_NAME = "deliveryAgents";
    public static final String COL1 = "ID";
    public static final String COL2 = "Capacity";

    public DeliveryAgentsDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null ,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " Capacity STRING)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String Capacity)
    {
        SQLiteDatabase db = MainActivity.deliveryAgentsDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, Capacity);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }
    boolean updateData(String ID, String Capacity)
    {
        SQLiteDatabase db = MainActivity.deliveryAgentsDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, ID);
        contentValues.put(COL2, Capacity);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{ID});
        return true;
    }

    Integer deleteData(String ID)
    {
        SQLiteDatabase db = MainActivity.deliveryAgentsDB.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{ID});
    }
    Cursor showData(){
        SQLiteDatabase db = MainActivity.deliveryAgentsDB.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        return data;
    }
    void deleteAll(){
        SQLiteDatabase db = MainActivity.deliveryAgentsDB.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
