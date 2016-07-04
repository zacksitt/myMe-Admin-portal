package com.eunovate.eunovatedev.myapp.dao_class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eunovate.eunovatedev.myapp.MainActivity;
import com.eunovate.eunovatedev.myapp.object.ScheduleObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by EunovateDev on 1/14/2016.
 */
public class Schedule_DBHelper{
    private SimpleDateFormat date_fmt=new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat datetime_fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat time_fmt=new SimpleDateFormat("HH:mm a");

    private static final SimpleDateFormat formatter=new SimpleDateFormat();
    protected SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private Context mContext;

    public Schedule_DBHelper(Context context){
        this.mContext=context;
        dbHelper=DataBaseHelper.getHelper(mContext);

        open();
    }

    public void open(){
        if(dbHelper==null)
            dbHelper=DataBaseHelper.getHelper(mContext);
        database=dbHelper.getWritableDatabase();
    }

    public boolean insertSchedule(ScheduleObject stuObj){

        //dbHelper.onUpgrade(database,1,2);
        ContentValues contentValues=new ContentValues();
        contentValues.put("schedule_id",stuObj.getSchedule_id());
        contentValues.put("class_id",stuObj.getClass_id());
        contentValues.put("course",stuObj.getCourse());
        contentValues.put("lesson",stuObj.getLesson());
        contentValues.put("level",stuObj.getLevel());
        contentValues.put("teacher",stuObj.getTeacher());
        contentValues.put("course_id",stuObj.getCourse_id());
        contentValues.put("date", date_fmt.format(stuObj.getDate()));
        contentValues.put("start_time", datetime_fmt.format(stuObj.getStart_time()));
        contentValues.put("end_time", datetime_fmt.format(stuObj.getEnd_time()));
        contentValues.put("vehicle",stuObj.getVehicle());
        contentValues.put("driver", stuObj.getDriver());
        contentValues.put("teacher_id",stuObj.getTeacher_id());
        contentValues.put("driver_id",stuObj.getDriver_id());
        contentValues.put("sent_to_server", 0);
        contentValues.put("recorded", stuObj.getRecorded());
       // contentValues.put("user", stuObj.getUser());
        database.insert("schedule", null, contentValues);
        return true;
    }

    public Boolean check_schedule(int sid){
        Cursor res=database.rawQuery("SELECT * FROM "+dbHelper.ATD_TABLE_NAME + " WHERE " +dbHelper.SCHEDULE_COLUMN_ID + " = " + sid , null);
        if(res.getCount()>0)
            return true;
        else
            return false;
    }

    public Cursor getScheduleDtl(int id){
        Cursor cursor=database.rawQuery("SELECT s.*,c."+dbHelper.CLASS_COLUMN_NAME+",c."+dbHelper.CLASS_COLUMN_ADDRESS + ",c." +dbHelper.CLASS_COLUMN_PHONE + " FROM " + dbHelper.SCHEDULE_TABLE_NAME + " as s LEFT JOIN "
                        +dbHelper.CLASS_TABLE_NAME + " as c ON c." + dbHelper.CLASS_COLUMN_ID + " = s." + dbHelper.CLASS_COLUMN_ID + " WHERE s."+dbHelper.SCHEDULE_COLUMN_ID + " = "+id,null);
        //Cursor res=database.rawQuery("select s.*,c.class_name from schedule s,class_tbl c where s.schedule_id" +"="+id+"",null);
        return cursor;
    }


    public ArrayList<ScheduleObject> get_schedule_list(String time){

        ArrayList<ScheduleObject> array_list=new ArrayList<ScheduleObject>();

        java.util.Date curdate= new java.util.Date();
        //hp=new HashMap();w
        String sql="";
        if(time.equals("today")) {
              //  sql="SELECT s.*,c.* FROM schedule s,class_tbl c WHERE strftime('%Y-%m-%d',s.date) >= date('now','-6 days') AND strftime('%Y-%m-%d',date)<=date('now') order by s.schedule_id";
              sql="SELECT s." + dbHelper.SCHEDULE_COLUMN_ID + ", s." + dbHelper.SCHEDULE_COLUMN_COURSE + ", s."
                    + dbHelper.SCHEDULE_COLUMN_LESSON + ", s." + dbHelper.SCHEDULE_COLUMN_DATE + ",s." + dbHelper.SCHEDULE_COLUMN_VEHICLE + ", c."+dbHelper.CLASS_COLUMN_LOCATION + " , c."
                      + dbHelper.CLASS_COLUMN_NAME + ",s." + dbHelper.SCHEDULE_COLUMN_STIME + ",s." + dbHelper.SCHEDULE_COLUMN_ETIME + " FROM "
                    +dbHelper.SCHEDULE_TABLE_NAME + " as s LEFT JOIN " +dbHelper.CLASS_TABLE_NAME + " as c ON s.class_id = c.class_id" +
                      //+ dbHelper.SCHEDULE_TABLE_NAME + " s , " + dbHelper.CLASS_TABLE_NAME + " c "+
                    "  WHERE  strftime('%Y-%m-%d',s." + dbHelper.SCHEDULE_COLUMN_DATE +") = date('now')  GROUP BY s. "+dbHelper.SCHEDULE_COLUMN_ID;

        }else if(time.equals("week")){
            sql="SELECT s." + dbHelper.SCHEDULE_COLUMN_ID + ", s." + dbHelper.SCHEDULE_COLUMN_COURSE + ", s."
                    + dbHelper.SCHEDULE_COLUMN_LESSON + ", s." + dbHelper.SCHEDULE_COLUMN_DATE + ", s." + dbHelper.SCHEDULE_COLUMN_VEHICLE + ",c."+dbHelper.CLASS_COLUMN_LOCATION + " , c."
                    + dbHelper.CLASS_COLUMN_NAME + ",s." + dbHelper.SCHEDULE_COLUMN_STIME + ",s." + dbHelper.SCHEDULE_COLUMN_ETIME + " FROM "
                    +dbHelper.SCHEDULE_TABLE_NAME + " as s LEFT JOIN " +dbHelper.CLASS_TABLE_NAME + " as c ON s.class_id=c.class_id" +
                    //+ dbHelper.SCHEDULE_TABLE_NAME + " s , " + dbHelper.CLASS_TABLE_NAME + " c "+
                    "  WHERE strftime('%Y-%m-%d',s." + dbHelper.SCHEDULE_COLUMN_DATE + ") <= date('now','+7 days') AND strftime('%Y-%m-%d', s."+dbHelper.SCHEDULE_COLUMN_DATE
                    + ")>=date('now') GROUP BY s. "+dbHelper.SCHEDULE_COLUMN_ID;

        }else if(time.equals("all")){
            sql="SELECT s." + dbHelper.SCHEDULE_COLUMN_ID + ", s." + dbHelper.SCHEDULE_COLUMN_COURSE + ", s."
                    + dbHelper.SCHEDULE_COLUMN_LESSON + ", s." + dbHelper.SCHEDULE_COLUMN_DATE + ", s." + dbHelper.SCHEDULE_COLUMN_VEHICLE + ",c."+dbHelper.CLASS_COLUMN_LOCATION + " , c."
                    + dbHelper.CLASS_COLUMN_NAME + ",s." + dbHelper.SCHEDULE_COLUMN_STIME + ",s." + dbHelper.SCHEDULE_COLUMN_ETIME + " FROM "
                    +dbHelper.SCHEDULE_TABLE_NAME + " as s LEFT JOIN " +dbHelper.CLASS_TABLE_NAME + " as c ON s.class_id=c.class_id" +
                    //+ dbHelper.SCHEDULE_TABLE_NAME + " s , " + dbHelper.CLASS_TABLE_NAME + " c "+
                    "  GROUP BY s. "+dbHelper.SCHEDULE_COLUMN_ID;

        }
        else{
            sql="SELECT s." + dbHelper.SCHEDULE_COLUMN_ID + ", s." + dbHelper.SCHEDULE_COLUMN_COURSE + ", s."
                    + dbHelper.SCHEDULE_COLUMN_LESSON + ", s." + dbHelper.SCHEDULE_COLUMN_DATE + ", s." + dbHelper.SCHEDULE_COLUMN_VEHICLE + ", c. " + dbHelper.CLASS_COLUMN_LOCATION + ", c."
                    + dbHelper.CLASS_COLUMN_NAME + ",s." + dbHelper.SCHEDULE_COLUMN_STIME + ",s." + dbHelper.SCHEDULE_COLUMN_ETIME + " FROM "
                    +dbHelper.SCHEDULE_TABLE_NAME + " as s LEFT JOIN " +dbHelper.CLASS_TABLE_NAME + " as c ON s.class_id=c.class_id " +
                   // + dbHelper.SCHEDULE_TABLE_NAME + " s , " + dbHelper.CLASS_TABLE_NAME + " c "+
                    "  WHERE strftime('%Y-%m-%d',s." + dbHelper.SCHEDULE_COLUMN_DATE + ") <= date('now','+30 days') AND strftime('%Y-%m-%d', s."+dbHelper.SCHEDULE_COLUMN_DATE
                    + ")>=date('now') GROUP BY s. "+dbHelper.SCHEDULE_COLUMN_ID;

        }

//        Cursor res=database.rawQuery("select s.schedule_id,s.course,s.lesson,s.date,c.class_name " +
//                "from schedule s,class_tbl c GROUP BY s.schedule_id WHERE ",null);
        Cursor res = database.rawQuery(sql, null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            ScheduleObject stuObj=new ScheduleObject();
            stuObj.setSchedule_id(res.getInt(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_ID)));
            stuObj.setClassname(res.getString(res.getColumnIndex(dbHelper.CLASS_COLUMN_NAME)));
            stuObj.setCourse(res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_COURSE)));
            stuObj.setLocation(res.getString(res.getColumnIndex(dbHelper.CLASS_COLUMN_LOCATION)));
            stuObj.setVehicle(res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_VEHICLE)));
            String date =res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_DATE));
            String stime=res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_STIME));
            String etime=res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_ETIME));

            try {
                stuObj.setDate(date_fmt.parse(date));
                stuObj.setStart_time(datetime_fmt.parse(stime));
                stuObj.setEnd_time(datetime_fmt.parse(etime));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            stuObj.setLesson(res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_LESSON)));
            //array_list.add(res.getString(res.getColumnIndex(STU_COLUMN_NAME)));
            array_list.add(stuObj);
            res.moveToNext();
        }
        if(!res.isClosed()){res.close();}
        return array_list;
    }
}
