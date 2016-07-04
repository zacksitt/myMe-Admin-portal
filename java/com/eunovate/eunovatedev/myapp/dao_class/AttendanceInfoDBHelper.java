package com.eunovate.eunovatedev.myapp.dao_class;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EunovateDev on 1/23/2016.
 */
public class AttendanceInfoDBHelper {
    protected SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private Context mContext;

    public AttendanceInfoDBHelper(Context context) {
        this.mContext = context;
        dbHelper = DataBaseHelper.getHelper(mContext);
        open();
    }

    public void open() {
        if (dbHelper == null)
            dbHelper = DataBaseHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

    public boolean set_record(int schedule_id){
        ContentValues contentValues=new ContentValues();
        contentValues.put("recorded", 1);
        database.update("schedule", contentValues, "schedule_id=?", new String[]{ Integer.toString(schedule_id)});
        return true;
    }


    public int getPresentStudent(int sid){
        int stuCount=0;
        Cursor res=database.rawQuery("SELECT  COUNT (" + dbHelper.STU_COLUMN_ID + ") AS stu_count FROM " + dbHelper.ATD_TABLE_NAME
                + " WHERE " + dbHelper.SCHEDULE_COLUMN_ID + " ="+sid,null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            stuCount=res.getInt(res.getColumnIndex("stu_count"));
            res.moveToNext();
        }
        return stuCount;
    }
}