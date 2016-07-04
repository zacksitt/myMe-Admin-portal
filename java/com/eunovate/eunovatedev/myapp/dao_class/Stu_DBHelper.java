package com.eunovate.eunovatedev.myapp.dao_class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eunovate.eunovatedev.myapp.object.StudentObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by EunovateDev on 1/7/2016.
 */
public class Stu_DBHelper {

    protected SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private Context mContext;
    private ContentValues contentValues;
    public Stu_DBHelper(Context context){
        this.mContext=context;
        dbHelper=DataBaseHelper.getHelper(mContext);
        open();
    }

    public void open(){
        if(dbHelper==null)
            dbHelper=DataBaseHelper.getHelper(mContext);
        database=dbHelper.getWritableDatabase();
    }

    public boolean insertStudent(StudentObject stuObj){

        contentValues=new ContentValues();
        contentValues.put("student_id",stuObj.getStudent_id());
        contentValues.put("name",stuObj.getName());
        contentValues.put("address",stuObj.getAddress());
        contentValues.put("contact",stuObj.getContact());
        contentValues.put("location",stuObj.getLocation());
        contentValues.put("gender",stuObj.getGender());
        contentValues.put("date_of_birth",stuObj.getDate_of_birth());
        contentValues.put("father_name",stuObj.getFather_name());
        contentValues.put("father_nrc_no",stuObj.getFather_nrc_no());
        contentValues.put("mother_name",stuObj.getMother_name());
        contentValues.put("mother_nrc_no",stuObj.getMother_nrc_no());
        contentValues.put("remark",stuObj.getRemark());
        contentValues.put("nrc_no",stuObj.getNrc_no());
        contentValues.put("is_active", stuObj.getIs_active());
        database.insert("student",null,contentValues);
        return true;
    }

     public boolean insertStuToClass(StudentObject stuObj,int course_id){
         contentValues=new ContentValues();
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
         Date cur_date=new Date();
         try {
             Date date = dateFormat.parse(stuObj.getDate_of_birth());
             contentValues.put("date_of_birth",dateFormat.format(date));
         } catch (ParseException e) {
             e.printStackTrace();
         }


        contentValues.put("name",stuObj.getName());
        contentValues.put("address",stuObj.getAddress());
        //contentValues.put("contact",stuObj.getContact());
        //contentValues.put("location",stuObj.getLocation());
        contentValues.put("gender",stuObj.getGender());
        //contentValues.put("date_of_birth",dateFormat.format());
        contentValues.put("father_name",stuObj.getFather_name());
        //contentValues.put("father_nrc_no",stuObj.getFather_nrc_no());
        contentValues.put("mother_name",stuObj.getMother_name());
         contentValues.put(dbHelper.STU_COLUMN_CDATE,dateFormat.format(cur_date));
         contentValues.put(dbHelper.STU_COLUMN_RADATE,dateFormat.format(cur_date));
        //contentValues.put("mother_nrc_no",stuObj.getMother_nrc_no());
        //contentValues.put("remark",stuObj.getRemark());
        //contentValues.put("nrc_no",stuObj.getNrc_no());
         contentValues.put("new_stu",1);
        contentValues.put("is_active", 1);
        long inserted_id=database.insert("student", null, contentValues);

         ContentValues contentStuClass=new ContentValues();
         contentStuClass.put("student_id", (int)inserted_id);
         contentStuClass.put("class_id", stuObj.getClass_id());
         contentStuClass.put("active_flag", 1);
         contentStuClass.put(dbHelper.CUR_COLUMN_ID,course_id);
         contentStuClass.put("new_stu",1);
         database.insert("student_class",null,contentStuClass);
         return true;
    }

    public Cursor getData(int id){
        Cursor res=database.rawQuery("select * from student where student_id="+id+"",null);
        return res;
    }

    public boolean updateContact (StudentObject stuObj){
        contentValues=new ContentValues();
        contentValues.put("name",stuObj.getName());
        contentValues.put("address",stuObj.getAddress());
        contentValues.put("contact",stuObj.getContact());
        contentValues.put("location",stuObj.getLocation());
        contentValues.put("gender",stuObj.getGender());
        contentValues.put("date_of_birth",stuObj.getDate_of_birth());
        contentValues.put("father_name",stuObj.getFather_name());
        contentValues.put("father_nrc_no",stuObj.getFather_nrc_no());
        contentValues.put("mother_name",stuObj.getMother_name());
        contentValues.put("mother_nrc_no",stuObj.getMother_nrc_no());
        contentValues.put("remark",stuObj.getRemark());
        contentValues.put("nrc_no",stuObj.getNrc_no());
        contentValues.put("is_active", stuObj.getIs_active());
        database.update("student", contentValues, "student_id=?", new String[]{ Integer.toString(stuObj.getStudent_id())});
        return true;
    }

    public Boolean update_sub_stu_activate(int stuid,int curid,int clsid){

            contentValues=new ContentValues();
            contentValues.put(dbHelper.STU_CLASS_COLUMN_FLAT,2);
            database.update(dbHelper.STU_CLASS_TABLE_NAME,contentValues,"student_id=? AND class_id=? AND course_id=?", new String[]{Integer.toString(stuid),Integer.toString(clsid),Integer.toString(curid)});
            return true;
    }

    public Boolean update_stu_activate(int stu_id,int key){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date cur_date=new Date();
        contentValues=new ContentValues();
        contentValues.put("is_active",key);
        contentValues.put("new_stu",2);
        contentValues.put("reactivated_date",dateFormat.format(cur_date));
        database.update("student",contentValues,"student_id=?", new String[]{Integer.toString(stu_id)});
        return true;
    }

    public ArrayList<StudentObject> get_inactive_students(int cid){
        ArrayList<StudentObject> array_list=new ArrayList<StudentObject>();
        Cursor res=database.rawQuery("SELECT stu." + dbHelper.STU_COLUMN_ID + ",stu." + dbHelper.STU_COLUMN_NAME + ",stu. " + dbHelper.STU_COLUMN_LOCATION +" FROM "
                + dbHelper.STU_TABLE_NAME +" stu, " + dbHelper.STU_CLASS_TABLE_NAME +" stu_cls WHERE stu." + dbHelper.STU_COLUMN_ISACTIVE + " = 1 AND stu."
                + dbHelper.STU_COLUMN_ID + " = stu_cls." +dbHelper.STU_COLUMN_ID + " AND stu_cls." + dbHelper.CLASS_COLUMN_ID + "="
                + cid + " ORDER BY stu_cls."+dbHelper.CLASS_COLUMN_ID,null);

        res.moveToFirst();
        while (res.isAfterLast()==false){
            StudentObject stuObj=new StudentObject();
            stuObj.setName(res.getString(res.getColumnIndex("name")));
            stuObj.setLocation(res.getString(res.getColumnIndex("location")));
            stuObj.setStudent_id(res.getInt(res.getColumnIndex("student_id")));
            array_list.add(stuObj);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<StudentObject> getAllStudents(){

        ArrayList<StudentObject> array_list=new ArrayList<StudentObject>();
        //hp=new HashMap();
        Cursor res=database.rawQuery("select * from student",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            StudentObject stuObj=new StudentObject();
            stuObj.setName(res.getString(res.getColumnIndex("name")));
            stuObj.setLocation(res.getString(res.getColumnIndex("location")));
            //array_list.add(res.getString(res.getColumnIndex(STU_COLUMN_NAME)));
            array_list.add(stuObj);
            res.moveToNext();
        }
        return array_list;
    }
}
