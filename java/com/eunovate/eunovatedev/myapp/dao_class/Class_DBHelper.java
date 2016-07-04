package com.eunovate.eunovatedev.myapp.dao_class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.eunovate.eunovatedev.myapp.object.ClassObject;
import com.eunovate.eunovatedev.myapp.object.StudentObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EunovateDev on 1/15/2016.
 */
public class Class_DBHelper{

    protected SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private Context mContext;
    private ContentValues contentValues;

    public Class_DBHelper(Context context){
        this.mContext=context;
        dbHelper=DataBaseHelper.getHelper(mContext);
        open();
    }

    public void open(){
        if(dbHelper==null)
            dbHelper=DataBaseHelper.getHelper(mContext);
        database=dbHelper.getWritableDatabase();
    }

    public boolean insert_class_student(JSONArray stu_list,int id,int courseid){
         try{
           // database.execSQL("DELETE FROM " +dbHelper.STU_CLASS_TABLE_NAME + " WHERE " + dbHelper.CLASS_COLUMN_ID + " = "+id);
            for (int i=0; i < stu_list.length(); i++){

                JSONObject stu_obj = stu_list.getJSONObject(i);
                Cursor res=database.rawQuery("SELECT * FROM " + dbHelper.STU_TABLE_NAME + " WHERE " + dbHelper.STU_COLUMN_ID + " = "+ stu_obj.getInt("student_id") ,null);

                if(res.getCount()< 1) {
                    contentValues = new ContentValues();
                    contentValues.put(dbHelper.STU_COLUMN_ID, stu_obj.getInt("student_id"));
                    contentValues.put(dbHelper.STU_COLUMN_NAME, stu_obj.getString("name"));
                    contentValues.put(dbHelper.STU_COLUMN_ADDRESS, stu_obj.getString("address"));
                    contentValues.put(dbHelper.STU_COLUMN_CONTACT, stu_obj.getString("contact"));
                    contentValues.put(dbHelper.STU_COLUMN_LOCATION, stu_obj.getString("location"));
                    contentValues.put(dbHelper.STU_COLUMN_GENDER, stu_obj.getString("gender"));
                    contentValues.put(dbHelper.STU_COLUMN_DATEOFBIRTH, stu_obj.getString("date_of_birth"));
                    contentValues.put(dbHelper.STU_COLUMN_NEW,0);
                    contentValues.put(dbHelper.STU_COLUMN_ISACTIVE,stu_obj.getString("is_active"));
                    database.insert(dbHelper.STU_TABLE_NAME,null,contentValues);
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put(dbHelper.STU_COLUMN_ID,stu_obj.getInt("student_id"));
                contentValues.put(dbHelper.CLASS_COLUMN_ID, id);
                contentValues.put(dbHelper.CUR_COLUMN_ID,courseid);
                contentValues.put(dbHelper.STU_COLUMN_NEW,0);
                contentValues.put(dbHelper.STU_CLASS_COLUMN_FLAT,stu_obj.getInt("subactive"));
                database.insert(dbHelper.STU_CLASS_TABLE_NAME, null, contentValues);
            }

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data" + e.toString());
        }

        return true;
    }

    public boolean insert(ClassObject Obj){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Cursor res=database.rawQuery("SELECT * FROM " + dbHelper.CLASS_TABLE_NAME + " WHERE " + dbHelper.CLASS_COLUMN_ID + " = " + Obj.getClass_id(),null);
        if(res.getCount()<1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("class_id", Obj.getClass_id());
            contentValues.put("class_name", Obj.getClassname());
            contentValues.put("location", Obj.getLocation());
            contentValues.put("teacher_id", Obj.getTeacher_id());
            contentValues.put("vehicle", Obj.getVehicle());

            if(Obj.getStart_date()!=null)
                contentValues.put("start_date",dateFormat.format(Obj.getStart_date()));
            if(Obj.getEnd_date()!=null)
                contentValues.put("end_date",dateFormat.format(Obj.getEnd_date()));

            contentValues.put("division",Obj.getDivision());
            contentValues.put("city",Obj.getCity());
            contentValues.put("contact_address",Obj.getAddress());
            contentValues.put("contact_phone",Obj.getPhone());
            database.insert("class_tbl", null, contentValues);
        }
            return true;

    }

    public ArrayList<StudentObject> getStuList(int id,int course_id){
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        //hp=new HashMap();
        Cursor res = database.rawQuery("SELECT stu."+dbHelper.STU_COLUMN_ID+" ,stu."+dbHelper.STU_COLUMN_NAME + " ,stu. " + dbHelper.STU_COLUMN_LOCATION + ",stu_cls." + dbHelper.CUR_COLUMN_ID +",stu_cls."+ dbHelper.STU_CLASS_COLUMN_FLAT
                + " FROM " + dbHelper.STU_TABLE_NAME + " as stu "
                + " LEFT JOIN " + dbHelper.STU_CLASS_TABLE_NAME + " as stu_cls ON stu." + dbHelper.STU_COLUMN_ID + " = stu_cls." + dbHelper.STU_COLUMN_ID
                + " WHERE stu_cls." + dbHelper.CLASS_COLUMN_ID + " = " +id + " AND stu_cls." + dbHelper.CUR_COLUMN_ID + " = " + course_id + " AND (stu_cls." + dbHelper.STU_CLASS_COLUMN_FLAT + " = 0 OR stu." + dbHelper.STU_COLUMN_ISACTIVE + "=0) GROUP BY stu." +dbHelper.STU_COLUMN_ID ,null);

//       Cursor res=database.rawQuery("SELECT stu." + dbHelper.STU_COLUMN_ID + ", stu."+dbHelper.STU_COLUMN_NAME + ", stu." + dbHelper.STU_COLUMN_LOCATION
//               + " FROM " + dbHelper.STU_TABLE_NAME + " as stu," + dbHelper.STU_CLASS_TABLE_NAME + " as stu_cls WHERE "
//               + " stu." +dbHelper.STU_COLUMN_ID + " = stu_cls." + dbHelper.STU_COLUMN_ID + " AND stu_cls." +dbHelper.CLASS_COLUMN_ID + " = " + 13 + " AND stu."
//               + dbHelper.STU_COLUMN_ISACTIVE + " = 0 OR stu_cls." + dbHelper.STU_CLASS_COLUMN_FLAT + " = 0",null);
       res.moveToFirst();
        ArrayList<StudentObject> arrayList=new ArrayList<>();
        while (res.isAfterLast()==false){

            StudentObject stuObj=new StudentObject();
            int check=res.getInt(res.getColumnIndex(dbHelper.STU_CLASS_COLUMN_FLAT));
            //Student Is Active value 1 for active,0 fro deactive,2 for subject
            if(check == 0){
                stuObj.setIs_active(2);
            }else{
                stuObj.setIs_active(0);
            }

            stuObj.setStudent_id(res.getInt(res.getColumnIndex(dbHelper.STU_COLUMN_ID)));
            stuObj.setCourse_id(res.getInt(res.getColumnIndex(dbHelper.CUR_COLUMN_ID)));
            stuObj.setName(res.getString(res.getColumnIndex(dbHelper.STU_COLUMN_NAME)));
            stuObj.setLocation(res.getString(res.getColumnIndex(dbHelper.STU_COLUMN_LOCATION)));
            arrayList.add(stuObj);
            res.moveToNext();
        }
        return arrayList;
    }

    public List<Map<String,String>> getSchList(int id){

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        //hp=new HashMap();
        Cursor res=database.rawQuery("SELECT * FROM " + dbHelper.SCHEDULE_TABLE_NAME + " WHERE " + dbHelper.SCHEDULE_COLUMN_CLASS + " = "+id ,null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            //String txt=res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_COURSE));
            //array_list.add(res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_COURSE)));
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("course", res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_COURSE)));
            datum.put("lesson", res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_LESSON)));
            data.add(datum);

            res.moveToNext();
        }
        return data;
    }

    public Cursor getClassDtl(int id){
        Cursor res=database.rawQuery("SELECT * FROM " +dbHelper.CLASS_TABLE_NAME +" WHERE " + dbHelper.CLASS_COLUMN_ID + " = " + id,null);
        return res;
    }

    public boolean insert_class_stu(){
        return true;
    }
}
