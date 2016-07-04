package com.eunovate.eunovatedev.myapp.dao_class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.eunovate.eunovatedev.myapp.object.Attendance_Record_Object;
import com.eunovate.eunovatedev.myapp.object.BehaviourObj;
import com.eunovate.eunovatedev.myapp.object.MaintenanceObject;
import com.eunovate.eunovatedev.myapp.object.ScheduleObject;
import com.eunovate.eunovatedev.myapp.object.StudentObject;
import com.eunovate.eunovatedev.myapp.object.VehicleObject;
import com.eunovate.eunovatedev.myapp.object.VehicleUsageObject;
import com.eunovate.eunovatedev.myapp.object.notiObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EunovateDev on 1/20/2016.
 */
public class AttendanceDBHelper {

    protected SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private Context mContext;
    private ContentValues contentValues;
    private SimpleDateFormat date_fmt;

    public AttendanceDBHelper(Context context){
        this.mContext=context;
        dbHelper=DataBaseHelper.getHelper(mContext);
        open();
    }
    public void open(){
        if(dbHelper==null)
            dbHelper=DataBaseHelper.getHelper(mContext);
        database=dbHelper.getWritableDatabase();
    }

    public ArrayList<MaintenanceObject> get_vehicle_mtn(){

        date_fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList<MaintenanceObject> arr_list=new ArrayList<>();
        Cursor cursor=database.rawQuery("SELECT * FROM " + dbHelper.VELMTN_TABLE_NAME +" WHERE " + dbHelper.VELMTN_COLUMN_SENT + " = 0 ",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            MaintenanceObject mtn_obj=new MaintenanceObject();
            mtn_obj.setVehicle(cursor.getInt(cursor.getColumnIndex(dbHelper.VEHICLE_COLUMN_ID)));
            mtn_obj.setOil(cursor.getInt(cursor.getColumnIndex(dbHelper.VELMTN_COLUMN_OIL)));
            mtn_obj.setCoolant(cursor.getInt(cursor.getColumnIndex(dbHelper.VELMTN_COLUMN_COOLANT)));
            mtn_obj.setAir(cursor.getInt(cursor.getColumnIndex(dbHelper.VELMTN_COLUMN_AIR)));
            mtn_obj.setComment(cursor.getString(cursor.getColumnIndex(dbHelper.VELMTN_COLUMN_COMMENT)));
            mtn_obj.setEngineoile(cursor.getInt(cursor.getColumnIndex(dbHelper.VELMTN_COLUMN_ENGOIL)));
            mtn_obj.setCar_body(cursor.getInt(cursor.getColumnIndex(dbHelper.VELMTN_COLUMN_CARBDY)));
            mtn_obj.setBrake(cursor.getInt(cursor.getColumnIndex(dbHelper.VELMTN_COLUMN_BRAKE)));
            mtn_obj.setLight(cursor.getInt(cursor.getColumnIndex(dbHelper.VELMTN_COLUMN_LIGHT)));
            mtn_obj.setFb_light(cursor.getInt(cursor.getColumnIndex(dbHelper.VELMTN_COLUMN_FBLIGHT)));
            mtn_obj.setWheel(cursor.getInt(cursor.getColumnIndex(dbHelper.VELMTN_COLUMN_WHEEL)));
            mtn_obj.setService(cursor.getInt(cursor.getColumnIndex(dbHelper.VELMTN_COLUMN_SERVICE)));
            String create_time=cursor.getString(cursor.getColumnIndex(dbHelper.VELMTN_COLUMN_CTIME));
            try {
                mtn_obj.setCreate_time(date_fmt.parse(create_time));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            arr_list.add(mtn_obj);
            cursor.moveToNext();
        }
        return arr_list;
    }

    public ArrayList<VehicleUsageObject> get_vehicle_usage(){
        date_fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList<VehicleUsageObject> arr_list=new ArrayList<>();
        Cursor res=database.rawQuery("SELECT * FROM " + dbHelper.VELUGE_TABLE_NAME + " WHERE " + dbHelper.VELUGE_COLUMN_SENT + " = 0 AND "
                + dbHelper.VELUGE_COLUMN_ENDOMTR + " > 0",null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            VehicleUsageObject obj=new VehicleUsageObject();
            obj.setVehicle_usage_id(res.getInt(res.getColumnIndex(dbHelper.VELUGE_COLUMN_ID)));
            obj.setVehicle_id(res.getInt(res.getColumnIndex(dbHelper.VEHICLE_COLUMN_ID)));
            obj.setStart_odometer(res.getInt(res.getColumnIndex(dbHelper.VELUGE_COLUMN_STODOMTR)));
            obj.setEnd_odometer(res.getInt(res.getColumnIndex(dbHelper.VELUGE_COLUMN_ENDOMTR)));
            String start_time=res.getString(res.getColumnIndex(dbHelper.VELUGE_COLUMN_STTIME));
            String end_time=res.getString(res.getColumnIndex(dbHelper.VELUGE_COLUMN_ENDTIME));

            try {
                obj.setStart_time(date_fmt.parse(start_time));
                obj.setEnd_tiem(date_fmt.parse(end_time));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            arr_list.add(obj);
            res.moveToNext();
        }
        return arr_list;
    }

    public JSONArray get_usage_line(int usage_id){

        JSONArray jarr = new JSONArray();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Cursor res = database.rawQuery("SELECT vul." + dbHelper.VELUGE_LINE_COLUMN_LOCATION + " , vul." +dbHelper.VELUGE_LINE_COLUMN_ADTIME + " , vul." + dbHelper.VELUGE_LINE_COLUMN_GPS + ",l."
                        + dbHelper.LOCATION_COLUMN_DESC + ",l." + dbHelper.LOCATION_COLUMN_NEWFLG + " FROM " +dbHelper.VELUGE_LINE_TABLE_NAME + " as vul " +
                        " LEFT JOIN " + dbHelper.LOCATION_TABLE_NAME + " as l ON vul." + dbHelper.LOCATION_COLUMN_ID + " = l." + dbHelper.LOCATION_COLUMN_ID + " WHERE vul."
                        + dbHelper.VELUGE_COLUMN_ID + " = " + usage_id,null);

//            Cursor res = database.rawQuery("SELECT " + dbHelper.VELUGE_LINE_COLUMN_LOCATION + " , " + dbHelper.VELUGE_LINE_COLUMN_ADTIME + " , " + dbHelper.VELUGE_LINE_COLUMN_GPS
//                    + " FROM " + dbHelper.VELUGE_LINE_TABLE_NAME + " WHERE " + dbHelper.VELUGE_COLUMN_ID + " = " + usage_id, null);
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                JSONObject jobj = new JSONObject();
                jobj.put("location_id", res.getInt(res.getColumnIndex(dbHelper.VELUGE_LINE_COLUMN_LOCATION)));
                jobj.put("added_time",res.getString(res.getColumnIndex(dbHelper.VELUGE_LINE_COLUMN_ADTIME)));
                jobj.put("vlocation_desc",res.getString(res.getColumnIndex(dbHelper.LOCATION_COLUMN_DESC)));
                jobj.put("vlct_newflag",res.getInt(res.getColumnIndex(dbHelper.LOCATION_COLUMN_NEWFLG)));

                if(res.getString(res.getColumnIndex(dbHelper.VELUGE_LINE_COLUMN_GPS))==null)
                    jobj.put("gps_location"," ");
                else
                    jobj.put("gps_location",res.getString(res.getColumnIndex(dbHelper.VELUGE_LINE_COLUMN_GPS)));

                jarr.put(jobj);
                res.moveToNext();
            }
        }catch (Exception e){
            Log.e("LOG TAG GET LINE EX",e.getMessage());
        }
        return jarr;
    }

    public void updated_vusage(){
        database.delete(dbHelper.VELUGE_TABLE_NAME,null,null);
        database.delete(dbHelper.VELUGE_LINE_TABLE_NAME,null,null);
       // ContentValues contentValues=new ContentValues();
        //contentValues.put(dbHelper.VELUGE_COLUMN_SENT, 1);
        //database.update(dbHelper.VELUGE_TABLE_NAME, contentValues, "sent_to_server=?", new String[]{Integer.toString(0)});
    }

    public void updated_vmtn(){
        database.delete(dbHelper.VELMTN_TABLE_NAME,null,null);
        //ContentValues contentValues=new ContentValues();
        //contentValues.put(dbHelper.VELMTN_COLUMN_SENT, 1);
        //database.update(dbHelper.VELMTN_TABLE_NAME, contentValues, "sent_to_server=?", new String[]{Integer.toString(0)});
    }

    public void insert_location(JSONObject jobj){
        try {
            contentValues = new ContentValues();
            contentValues.put(dbHelper.LOCATION_COLUMN_ID,jobj.getInt("vlocation_id"));
            contentValues.put(dbHelper.LOCATION_COLUMN_DESC, jobj.getString("v_location_desc"));
            contentValues.put(dbHelper.LOCATION_COLUMN_NEWFLG,0);
            database.insert("location", null, contentValues);

        }catch (Exception e){
            //return new String("Exception: "+e.getMessage());
            Log.e("Log Tag ADD LCT : ","Json retriving error");
        }
    }

    public void insert_driver_schedule(ScheduleObject object){

        contentValues=new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        contentValues.put("schedule_id",object.getSchedule_id());
        contentValues.put("date",dateFormat.format(object.getDate()));
        contentValues.put("start_time",dateFormat.format(object.getStart_time()));
        contentValues.put("end_time",dateFormat.format(object.getEnd_time()));
        contentValues.put("teacher",object.getTeacher());
        contentValues.put("class_id",object.getClass_id());
        contentValues.put("teacher_id",object.getTeacher_id());
        contentValues.put("driver",object.getDriver());
        contentValues.put("driver_id",object.getDriver_id());
        contentValues.put("vehicle",object.getVehicle());
        database.insert("schedule",null,contentValues);
    }

    public void insert_vehicle(VehicleObject obj){
        Cursor res=database.rawQuery("SELECT * FROM " +dbHelper.VEHICLE_TABLE_NAME + " WHERE " + dbHelper.VEHICLE_COLUMN_ID + " = " +obj.getVehicle_id() ,null);
        if (res.getCount()< 1){
            contentValues = new ContentValues();
            contentValues.put("vehicle_id",obj.getVehicle_id());
            contentValues.put("no",obj.getVehicle_no());
            contentValues.put("model",obj.getVehicle_model());
            database.insert("vehicle",null,contentValues);
        }
    }

    public boolean check_update_driver_data(){
        Cursor r=database.rawQuery("SELECT * FROM " + dbHelper.VELUGE_TABLE_NAME + " WHERE " +dbHelper.VELUGE_COLUMN_SENT + " = 0",null);
        Cursor rr=database.rawQuery("SELECT * FROM " + dbHelper.VELMTN_TABLE_NAME + " WHERE " + dbHelper.VELMTN_COLUMN_SENT + " = 0", null);
        if(r.getCount()>0 || rr.getCount()>0){
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<StudentObject> get_stu_list(int id,int course_id){
        ArrayList<StudentObject> stu_list=new ArrayList<>();

        Cursor res=database.rawQuery("SELECT stu_cls." + dbHelper.STU_CLASS_COLUMN_FLAT + " ,stu. " + dbHelper.STU_COLUMN_ID + ",stu." + dbHelper.STU_COLUMN_ADDRESS+" , stu. " +dbHelper.STU_COLUMN_NEW + ",stu." + dbHelper.STU_COLUMN_ID + ", stu. " + dbHelper.STU_COLUMN_NAME + " " +
                " FROM " + dbHelper.STU_TABLE_NAME + " as stu"
                + " LEFT JOIN " + dbHelper.STU_CLASS_TABLE_NAME + " as stu_cls ON stu." +dbHelper.STU_COLUMN_ID + " = stu_cls." + dbHelper.STU_COLUMN_ID +
                " WHERE stu. " + dbHelper.STU_COLUMN_ISACTIVE + " = 1 AND stu_cls."
                + dbHelper.CLASS_COLUMN_ID + " = " + id + " AND stu_cls." + dbHelper.CUR_COLUMN_ID + " = " + course_id + " AND stu_cls." + dbHelper.STU_CLASS_COLUMN_FLAT + " > " + 0, null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            StudentObject stuObj=new StudentObject();
            int check=res.getInt(res.getColumnIndex(dbHelper.STU_CLASS_COLUMN_FLAT));
            //Student Is Active value 1 for active,0 fro deactive,2 for subject
            if(check == 2){
                stuObj.setIs_active(2);
            }else{
                stuObj.setIs_active(0);
            }

            stuObj.setStu_new(res.getInt(res.getColumnIndex(dbHelper.STU_COLUMN_NEW)));
            stuObj.setStudent_id(res.getInt(res.getColumnIndex(dbHelper.STU_COLUMN_ID)));
            stuObj.setName(res.getString(res.getColumnIndex(dbHelper.STU_COLUMN_NAME)));
            stuObj.setAtd_finished(0);
            stuObj.setLocation(res.getString(res.getColumnIndex(dbHelper.STU_COLUMN_ADDRESS)));
            //array_list.add(res.getString(res.getColumnIndex(STU_COLUMN_NAME)));
            stu_list.add(stuObj);
            res.moveToNext();
        }
        return stu_list;
    }

    public boolean set_schedule_recorded(int schedule_id){
        ContentValues contentValues=new ContentValues();
        contentValues.put("recorded", 1);
        database.update("schedule", contentValues, "schedule_id=?", new String[]{Integer.toString(schedule_id)});
        return true;
    }

    public int insertAttendance(int schid,Attendance_Record_Object atdobj){
        contentValues=new ContentValues();
        contentValues.put("student_id", atdobj.getStudent_id());
        contentValues.put("schedule_id", schid);
        contentValues.put("present_flag",atdobj.getPresent_flag());
        contentValues.put("comment", atdobj.getComment());
        // stu_chk for student status. 1 for new student,2 for activate student,3 for activate for student in class
        contentValues.put(dbHelper.ATD_COLUMN_STU_CHK,atdobj.getStu_chk());
        int inserted_id=(int)database.insert("attendance",null,contentValues);
        return inserted_id;
    }

    public Boolean sent_noti(){
        contentValues=new ContentValues();
        contentValues.put("seen",2);
        database.update("notification", contentValues, "seen=?", new String[]{Integer.toString(1)});
        return true;
    }

    public Boolean seen_noti(){
        contentValues=new ContentValues();
        contentValues.put("seen", 1);
        database.update("notification", contentValues, "seen=?", new String[]{Integer.toString(0)});
        return true;
    }

    public Boolean finished_post(){
        contentValues=new ContentValues();
        contentValues.put("sent_to_server", 1);
        database.update("schedule", contentValues, "recorded=?", new String[]{Integer.toString(1)});

        database.delete(dbHelper.ATD_TABLE_NAME, null, null);
        database.delete(dbHelper.BVR_RCD_TABLE_NAME, null, null);
        return true;
    }

    public JSONArray get_stu_atd_rcd(int atd_id){
        JSONArray jarr=new JSONArray();
        ArrayList<Attendance_Record_Object> atd_rcd_list=new ArrayList<>();
        Cursor res=database.rawQuery("SELECT * FROM " +dbHelper.BVR_RCD_TABLE_NAME + " WHERE " + dbHelper.ATD_COLUMN_ID + " = " + atd_id ,null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            try {
                JSONObject jobj=new JSONObject();
                jobj.put("behaviour_id",res.getInt(res.getColumnIndex(dbHelper.BVR_COLUMN_ID)));
                jobj.put("attendance_id",res.getInt(res.getColumnIndex(dbHelper.ATD_COLUMN_ID)));
                jobj.put("rating",res.getInt(res.getColumnIndex(dbHelper.BVR_RCD_COLUMN_RATING)));
                jarr.put(jobj);
                res.moveToNext();
            }
            catch (Exception e){
                //return new String("Exception: "+e.getMessage());
                jarr.put(new String(e.getMessage()));
            }
        }

        return jarr;
    }

    public ArrayList<StudentObject> get_new_student(){


        ArrayList<StudentObject> arr_list=new ArrayList<>();
        Cursor cursor=database.rawQuery("SELECT stu.*,stu_cls." + dbHelper.STU_CLASS_COLUMN_FLAT + " ,stu_cls." + dbHelper.CUR_COLUMN_ID + " ,stu_cls. " + dbHelper.CLASS_COLUMN_ID + ",atd. " + dbHelper.ATD_COLUMN_COMMENT + ",atd." + dbHelper.SCHEDULE_COLUMN_ID+ ", atd."
                + dbHelper.ATD_COLUMN_PRESENT + " ,atd."+ dbHelper.ATD_COLUMN_ID +
                " FROM " + dbHelper.STU_TABLE_NAME + " as stu" +
                " LEFT JOIN " + dbHelper.STU_CLASS_TABLE_NAME + " as stu_cls ON stu." + dbHelper.STU_COLUMN_ID + " = stu_cls." + dbHelper.STU_COLUMN_ID +
                " LEFT JOIN " + dbHelper.ATD_TABLE_NAME + " as atd ON stu." + dbHelper.STU_COLUMN_ID + " = atd." + dbHelper.STU_COLUMN_ID +
                " WHERE stu." +dbHelper.STU_COLUMN_NEW + " > 0 OR stu_cls. " + dbHelper.STU_CLASS_COLUMN_FLAT + " = 2 " + " GROUP BY stu." + dbHelper.STU_COLUMN_ID,null);

        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            StudentObject obj=new StudentObject();

            // Check active in subject , 1 active, 2 activate by app
            obj.setActiv_in_subject(cursor.getInt(cursor.getColumnIndex(dbHelper.STU_CLASS_COLUMN_FLAT)));
            obj.setComment(cursor.getString(cursor.getColumnIndex(dbHelper.ATD_COLUMN_COMMENT)));
            obj.setClass_id(cursor.getInt(cursor.getColumnIndex(dbHelper.CLASS_COLUMN_ID)));
            obj.setStu_new(cursor.getInt(cursor.getColumnIndex(dbHelper.STU_COLUMN_NEW)));
            obj.setIs_active(cursor.getInt(cursor.getColumnIndex(dbHelper.STU_COLUMN_ISACTIVE)));
            obj.setStudent_id(cursor.getInt(cursor.getColumnIndex(dbHelper.STU_COLUMN_ID)));
            obj.setCreated_date(cursor.getString(cursor.getColumnIndex(dbHelper.STU_COLUMN_CDATE)));
            obj.setReactivated_date(cursor.getString(cursor.getColumnIndex(dbHelper.STU_COLUMN_RADATE)));
            obj.setName(cursor.getString(cursor.getColumnIndex(dbHelper.STU_COLUMN_NAME)));
            obj.setAddress(cursor.getString(cursor.getColumnIndex(dbHelper.STU_COLUMN_ADDRESS)));
            obj.setPresent_flag(cursor.getInt(cursor.getColumnIndex(dbHelper.ATD_COLUMN_PRESENT)));
            obj.setGender(cursor.getInt(cursor.getColumnIndex(dbHelper.STU_COLUMN_GENDER)));
            obj.setDate_of_birth(cursor.getString(cursor.getColumnIndex(dbHelper.STU_COLUMN_DATEOFBIRTH)));
            obj.setFather_name(cursor.getString(cursor.getColumnIndex(dbHelper.STU_COLUMN_FATHERNAME)));
            obj.setMother_name(cursor.getString(cursor.getColumnIndex(dbHelper.STU_COLUMN_MOTHERNAME)));
            obj.setAttendance(cursor.getInt(cursor.getColumnIndex(dbHelper.ATD_COLUMN_ID)));
            obj.setSchedule_id(cursor.getInt(cursor.getColumnIndex(dbHelper.SCHEDULE_COLUMN_ID)));
            obj.setCourse_id(cursor.getInt(cursor.getColumnIndex(dbHelper.CUR_COLUMN_ID)));
            obj.setGender(cursor.getInt(cursor.getColumnIndex(dbHelper.STU_COLUMN_GENDER)));
            obj.setAddress(cursor.getString(cursor.getColumnIndex(dbHelper.STU_COLUMN_ADDRESS)));
            arr_list.add(obj);
            cursor.moveToNext();
        }
        return arr_list;
    }

    public Cursor get_new_cls_stu(){
        Cursor cursor=database.rawQuery("SELECT * FROM " +dbHelper.STU_CLASS_TABLE_NAME + " WHERE " +dbHelper.STU_COLUMN_NEW + " =1",null);
        return cursor;
    }

    public ArrayList<Attendance_Record_Object> get_attendance_record(){

        ArrayList<Attendance_Record_Object> atd_rcd_list=new ArrayList<>();
        Cursor res=database.rawQuery("SELECT atd. " + dbHelper.ATD_COLUMN_ID + ", atd." + dbHelper.ATD_COLUMN_COMMENT + ", atd. " + dbHelper.SCHEDULE_COLUMN_ID + ",atd."
                + dbHelper.STU_COLUMN_ID + " , bvr_rcd. " + dbHelper.BVR_COLUMN_ID + " , bvr_rcd. " + dbHelper.BVR_RCD_COLUMN_RATING + ",atd."
                + dbHelper.ATD_COLUMN_PRESENT + " FROM " + dbHelper.SCHEDULE_TABLE_NAME + " as s LEFT JOIN " + dbHelper.ATD_TABLE_NAME + " as atd ON s." +dbHelper.SCHEDULE_COLUMN_ID + " = atd."
                + dbHelper.SCHEDULE_COLUMN_ID + " LEFT JOIN " + dbHelper.BVR_RCD_TABLE_NAME + " as bvr_rcd ON atd. " + dbHelper.ATD_COLUMN_ID + " = bvr_rcd. "
                + dbHelper.ATD_COLUMN_ID + " WHERE s." +dbHelper.SCHEDULE_COLUMN_SENT + " = 0 AND s. " + dbHelper.SCHEDULE_COLUMN_RECORDED + " = 1 AND atd. "
                + dbHelper.ATD_COLUMN_STU_CHK + " = 0 ORDER BY atd." + dbHelper.ATD_COLUMN_ID,null);
               res.moveToFirst();

        while (res.isAfterLast()==false){
            Attendance_Record_Object obj=new Attendance_Record_Object();
            obj.setAttendance_id(res.getInt(res.getColumnIndex(dbHelper.ATD_COLUMN_ID)));
            obj.setSchedule_id(res.getInt(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_ID)));
            obj.setStudent_id(res.getInt(res.getColumnIndex(dbHelper.STU_COLUMN_ID)));
            obj.setPresent_flag(res.getInt(res.getColumnIndex(dbHelper.ATD_COLUMN_PRESENT)));

            obj.setComment(res.getString(res.getColumnIndex(dbHelper.ATD_COLUMN_COMMENT)));
            obj.setBehaviour_id(res.getInt(res.getColumnIndex(dbHelper.BVR_COLUMN_ID)));
            obj.setRating(res.getInt(res.getColumnIndex(dbHelper.BVR_RCD_COLUMN_RATING)));
            atd_rcd_list.add(obj);
            res.moveToNext();
        }
        return atd_rcd_list;
    }

//    public int get_sync_schedule_count(){
//        int count=0;
//        Cursor res=database.rawQuery("SELECT " + dbHelper.SYNC_COUNT_COLUMN_COUNT+ " FROM " + dbHelper.SYNC_COUNT_TABLE_NAME + " WHERE " + dbHelper.SYNC_COUNT_COLUMN_CATID + " = 1 " ,null);
//        res.moveToFirst();
//        while (res.isAfterLast()==false){
//            count=res.getInt(res.getColumnIndex(dbHelper.SYNC_COUNT_COLUMN_COUNT));
//            res.moveToNext();
//        }
//       // database.close();
//        return count;
//    }

    public Boolean upd_schedule_sync_count(String sync_count){
        contentValues=new ContentValues();
        contentValues.put("last_sync_count", Integer.parseInt(sync_count));
        long update_id = database.update("last_sync", contentValues, "sync_category_id=?", new String[]{"1"});
        if((int) update_id < 1){
            contentValues.put("category", "schedule");
            contentValues.put("sync_category_id",1);
            database.insert("last_sync",null,contentValues);
        }
        return true;
    }

    public ArrayList<String> get_cmt_list(){
        ArrayList<String> cmt_list=new ArrayList<>();
        Cursor res=database.rawQuery("SELECT * FROM " + dbHelper.COMMENT_TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            cmt_list.add(res.getString(res.getColumnIndex(dbHelper.COMMENT_DESC)));
            res.moveToNext();
        }
        return cmt_list;
    }

    public boolean insertAttendanceRcd(ArrayList<BehaviourObj> bList,int atd_id){

        for (BehaviourObj bObj : bList) {
            contentValues = new ContentValues();
            contentValues.put("behaviour_id", bObj.getBehaviourID());
            contentValues.put("attendance_id", atd_id);
            contentValues.put("rating", Integer.toString(bObj.getRating()));
            database.insert("behaviour_record", null, contentValues);
        }
        return true;
    }

    public boolean insert_location(VehicleUsageObject obj){

        Date date=new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(obj.getLocation_id()<1){
            contentValues=new ContentValues();
            contentValues.put(dbHelper.LOCATION_COLUMN_DESC,obj.getLocation_desc());
            contentValues.put(dbHelper.LOCATION_COLUMN_NEWFLG, 1);
            obj.setLocation_id((int)database.insert(dbHelper.LOCATION_TABLE_NAME,null,contentValues));
        }

        contentValues=new ContentValues();
        contentValues.put(dbHelper.VELUGE_COLUMN_ID,obj.getVehicle_usage_id());
        contentValues.put(dbHelper.LOCATION_COLUMN_ID,obj.getLocation_id());
        contentValues.put(dbHelper.VELUGE_LINE_COLUMN_ADTIME,dateFormat.format(date));
        contentValues.put(dbHelper.VELUGE_LINE_COLUMN_GPS,obj.getGps_location());
        contentValues.put(dbHelper.VELUGE_LINE_COLUMN_ACTFLG, 0);
        long res=database.insert(dbHelper.VELUGE_LINE_TABLE_NAME,null,contentValues);

        if((int)res>0){
            return true;
        }else{
            return false;
        }
    }

    public int get_noti_count(){
        Cursor res=database.rawQuery("SELECT * FROM " + dbHelper.NOTI_TABLE_NAME + " WHERE " + dbHelper.NOTI_COLUMN_SEEN + " = 0",null);
        return res.getCount();
    }

    public int update_vehicle_usage(VehicleUsageObject obj,int end_chk){
        contentValues=new ContentValues();
        Date date=new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(end_chk>0){
            contentValues.put("end_time",dateFormat.format(date));
            contentValues.put("sent_to_server",0);
        }else{
            contentValues.put("start_odometer", obj.getStart_odometer());
        }
        contentValues.put("end_odometer",obj.getEnd_odometer());
        return (int)database.update("vehicle_usage", contentValues, "vehicle_usage_id=?", new String[]{Integer.toString(obj.getVehicle_usage_id())});
    }


    public int insert_vehicle_usage(VehicleUsageObject object){
        contentValues=new ContentValues();
        Date date=new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        contentValues.put("vehicle_id",object.getVehicle_id());
        contentValues.put("end_odometer",object.getEnd_odometer());
        contentValues.put("start_time",dateFormat.format(date));
        contentValues.put("start_odometer", object.getStart_odometer());

        long insert_id=database.insert("vehicle_usage",null,contentValues);
        return (int) insert_id;
    }

    public Boolean clear_class(){
        database.delete(dbHelper.CLASS_TABLE_NAME, null, null);
        database.delete(dbHelper.STU_CLASS_TABLE_NAME,null,null);
        return true;
    }

    public Boolean clear_comment(){
        database.delete(dbHelper.COMMENT_TABLE_NAME,null,null);
        return true;
    }

    public Boolean clear_behaviour(){
        database.delete(dbHelper.BVR_TABLE_NAME,null,null);
        return true;
    }

    public Boolean clear_student(){
        database.delete(dbHelper.STU_TABLE_NAME, null, null);
        return true;
    }

    public Boolean clear_vehicle(){
        database.execSQL("delete from " + dbHelper.VEHICLE_TABLE_NAME);
        return true;
    }

    public Boolean clear_noti_data(){
        database.execSQL("delete from " + dbHelper.NOTI_TABLE_NAME);
        return true;
    }

    public Boolean clear_schedule(){
        database.execSQL("DELETE FROM " + dbHelper.SCHEDULE_TABLE_NAME);
        return true;
    }

    public Boolean clear_location(){
        database.execSQL("DELETE FROM " + dbHelper.LOCATION_TABLE_NAME);
        return true;
    }

    public void insert_behaviour(BehaviourObj bObj){
        contentValues=new ContentValues();
        contentValues.put("behaviour_id",bObj.getBehaviourID());
        contentValues.put("description",bObj.getBehaviour());
        database.insert("behaviour", null, contentValues);
    }

    public void insert_comment(JSONObject obj){
         try {

             contentValues=new ContentValues();
            contentValues.put("comment_id",obj.getInt("comment_id"));
            contentValues.put("description",obj.getString("comment_desc"));
            database.insert("comment", null, contentValues);

        }catch (Exception e){

        }
    }

    public ArrayList<notiObject> get_post_noti_list(){

        ArrayList<notiObject> arrayList=new ArrayList<>();
        notiObject notiobj;
        //hp=new HashMap();
        Cursor res=database.rawQuery("SELECT * FROM " + dbHelper.NOTI_TABLE_NAME + " WHERE " + dbHelper.NOTI_COLUMN_SEEN + " = 1",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            notiobj=new notiObject();
            //String txt=res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_COURSE));
            notiobj.setNoti_id(res.getInt(res.getColumnIndex(dbHelper.NOTI_COLUMN_ID)));
            arrayList.add(notiobj);
            res.moveToNext();
        }
        return arrayList;
    }

    public List<Map<String,String>> get_noti_list(){
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        //hp=new HashMap();
        Cursor res=database.rawQuery("SELECT * FROM " + dbHelper.NOTI_TABLE_NAME + " WHERE " + dbHelper.NOTI_COLUMN_SEEN + " = 0",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            //String txt=res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_COURSE));
            //array_list.add(res.getString(res.getColumnIndex(dbHelper.SCHEDULE_COLUMN_COURSE)));
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("description", res.getString(res.getColumnIndex(dbHelper.NOTI_COLUMN_DESC)));
            datum.put("date", res.getString(res.getColumnIndex(dbHelper.NOTI_COLUMN_DATETIME)));
            data.add(datum);
            res.moveToNext();
        }
        return data;
    }

    public boolean insert_noti(notiObject nobj,int uid){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        contentValues=new ContentValues();
        contentValues.put("noti_id",nobj.getNoti_id());
        contentValues.put("date_time",dateFormat.format(nobj.getDate()));
        contentValues.put("description",nobj.getDescription());
        contentValues.put("seen",nobj.getSeen());
        contentValues.put("user_id",uid);
        database.insert("notification", null, contentValues);
        return true;
    }

    public boolean insert_maintenance(MaintenanceObject obj){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        contentValues=new ContentValues();
        contentValues.put(dbHelper.VEHICLE_COLUMN_ID,obj.getVehicle());
        contentValues.put(dbHelper.VELMTN_COLUMN_OIL,obj.getOil());
        contentValues.put(dbHelper.VELMTN_COLUMN_COOLANT,obj.getCoolant());
        contentValues.put(dbHelper.VELMTN_COLUMN_AIR,obj.getAir());
        contentValues.put(dbHelper.VELMTN_COLUMN_ENGOIL,obj.getEngineoile());
        contentValues.put(dbHelper.VELMTN_COLUMN_CARBDY,obj.getCar_body());
        contentValues.put(dbHelper.VELMTN_COLUMN_BRAKE,obj.getBrake());
        contentValues.put(dbHelper.VELMTN_COLUMN_LIGHT,obj.getLight());
        contentValues.put(dbHelper.VELMTN_COLUMN_FBLIGHT,obj.getFb_light());
        contentValues.put(dbHelper.VELMTN_COLUMN_WHEEL,obj.getWheel());
        contentValues.put(dbHelper.VELMTN_COLUMN_SERVICE,obj.getService());
        contentValues.put(dbHelper.VELMTN_COLUMN_COMMENT,obj.getComment());
        contentValues.put(dbHelper.VELMTN_COLUMN_SENT,0);
        contentValues.put(dbHelper.VELMTN_COLUMN_CTIME,dateFormat.format(date));
        database.insert("vehicle_maintenance", null, contentValues);
        return true;
    }

    public boolean check_stu_update(){
        Cursor res=database.rawQuery("SELECT * FROM " + dbHelper.STU_TABLE_NAME + " WHERE " + dbHelper.STU_COLUMN_NEW + " > 0",null);
        if (res.getCount()>0){
            return true;
        }else{
            return false;
        }
    }

    public boolean check_update(){
        Cursor res=database.rawQuery("SELECT * FROM " + dbHelper.SCHEDULE_TABLE_NAME + " WHERE "+dbHelper.SCHEDULE_COLUMN_RECORDED +
                "=1 AND "+dbHelper.SCHEDULE_COLUMN_SENT + " = 0",null);
        if (res.getCount()>0){
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<VehicleObject> get_vlist(){
        ArrayList<VehicleObject> array_list=new ArrayList<>();
        Cursor res;
            res=database.rawQuery("SELECT vl." + dbHelper.VELUGE_COLUMN_ID + ", v." + dbHelper.VEHICLE_COLUMN_ID + ",v." + dbHelper.VEHICLE_COLUMN_NO + " FROM " + dbHelper.VEHICLE_TABLE_NAME + " v,"
                    +dbHelper.VELUGE_TABLE_NAME +" vl WHERE v." + dbHelper.VEHICLE_COLUMN_ID + "= vl."+dbHelper.VEHICLE_COLUMN_ID+" AND vl."
                    + dbHelper.VELUGE_COLUMN_ENDOMTR+" = 0",null);

        if(res.getCount()==0){
            res=database.rawQuery("SELECT " + dbHelper.VEHICLE_COLUMN_ID + "," + dbHelper.VEHICLE_COLUMN_NO + " FROM " + dbHelper.VEHICLE_TABLE_NAME,null);
            res.moveToFirst();
            while (res.isAfterLast()==false){
                VehicleObject vobj=new VehicleObject();
                //array_list.add(res.getString(res.getColumnIndex(STU_COLUMN_NAME)));
                vobj.setDoingChk(0);
                vobj.setVehicle_usgae_id(0);
                vobj.setVehicle_id(res.getInt(res.getColumnIndex(dbHelper.VEHICLE_COLUMN_ID)));
                vobj.setVehicle_no(res.getString(res.getColumnIndex(dbHelper.VEHICLE_COLUMN_NO)));
                array_list.add(vobj);
                res.moveToNext();
            }
        }else{
            res.moveToFirst();
            while (res.isAfterLast()==false){
                VehicleObject vobj=new VehicleObject();
                //array_list.add(res.getString(res.getColumnIndex(STU_COLUMN_NAME)));
                vobj.setDoingChk(1);
                vobj.setVehicle_usgae_id(res.getInt(res.getColumnIndex(dbHelper.VELUGE_COLUMN_ID)));
                vobj.setVehicle_id(res.getInt(res.getColumnIndex(dbHelper.VEHICLE_COLUMN_ID)));
                vobj.setVehicle_no(res.getString(res.getColumnIndex(dbHelper.VEHICLE_COLUMN_NO)));
                array_list.add(vobj);
                res.moveToNext();
            }
        }


        return array_list;
    }

    public VehicleUsageObject get_vusage_dtl(int vid,int vuid){

        Cursor res=database.rawQuery("SELECT u." + dbHelper.VELUGE_COLUMN_STODOMTR + ",count(ul." + dbHelper.LOCATION_COLUMN_ID + ") as count FROM "
                +dbHelper.VELUGE_TABLE_NAME + " as u LEFT JOIN "
                +dbHelper.VELUGE_LINE_TABLE_NAME + " as ul ON ul."+dbHelper.VELUGE_COLUMN_ID + " = u." +dbHelper.VELUGE_COLUMN_ID + " WHERE u."
                + dbHelper.VELUGE_COLUMN_ID + " = " + vuid + " AND u." +dbHelper.VEHICLE_COLUMN_ID + " = " + vid,null);

        res.moveToFirst();
        VehicleUsageObject obj=new VehicleUsageObject();
        while (res.isAfterLast()==false){

            obj.setStart_odometer(res.getInt(res.getColumnIndex(dbHelper.VELUGE_COLUMN_STODOMTR)));
            obj.setLocation_count(res.getInt(res.getColumnIndex("count")));
            res.moveToNext();
        }
        return obj;
    }

    public ArrayList<VehicleUsageObject> get_location(int vid){

        ArrayList<VehicleUsageObject> array_list=new ArrayList<>();
        Cursor res;
        if(vid>0){
            res=database.rawQuery("SELECT l." + dbHelper.LOCATION_COLUMN_DESC + ",l. " + dbHelper.LOCATION_COLUMN_ID + " FROM " +dbHelper.VELUGE_TABLE_NAME + " u,"
                    + dbHelper.VELUGE_LINE_TABLE_NAME + " ul," +dbHelper.LOCATION_TABLE_NAME + " l WHERE u."+dbHelper.VELUGE_COLUMN_ID + " = ul. " + dbHelper.VELUGE_COLUMN_ID + " AND ul."
                    + dbHelper.LOCATION_COLUMN_ID + " = l." +dbHelper.LOCATION_COLUMN_ID + " AND u." + dbHelper.VELUGE_COLUMN_ID + "= "+vid,null);

        }else{
            res=database.rawQuery("SELECT "+dbHelper.LOCATION_COLUMN_ID+ ","+dbHelper.LOCATION_COLUMN_DESC + " FROM "+dbHelper.LOCATION_TABLE_NAME,null);
        }

        //Cursor res=database.rawQuery("SELECT " + dbHelper.LOCATION_COLUMN_ID + "," + dbHelper.LOCATION_COLUMN_DESC + " FROM " + dbHelper.LOCATION_TABLE_NAME ,null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            VehicleUsageObject obj=new VehicleUsageObject();
            obj.setLocation_desc(res.getString(res.getColumnIndex(dbHelper.LOCATION_COLUMN_DESC)));
            obj.setLocation_id(res.getInt(res.getColumnIndex(dbHelper.LOCATION_COLUMN_ID)));

            array_list.add(obj);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<BehaviourObj> get_bvr_list(){

        ArrayList<BehaviourObj> array_list=new ArrayList<BehaviourObj>();
        //hp=new HashMap();
        Cursor res=database.rawQuery("SELECT " + dbHelper.BVR_COLUMN_DESC + "," + dbHelper.BVR_COLUMN_ID + " FROM " + dbHelper.BVR_TABLE_NAME ,null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            BehaviourObj behaviourObj=new BehaviourObj();
            //array_list.add(res.getString(res.getColumnIndex(STU_COLUMN_NAME)));
            behaviourObj.setBehaviourID(res.getInt(res.getColumnIndex(dbHelper.BVR_COLUMN_ID)));
            behaviourObj.setBehaviour(res.getString(res.getColumnIndex(dbHelper.BVR_COLUMN_DESC)));
            behaviourObj.setRating(3);
            array_list.add(behaviourObj);
            res.moveToNext();
        }
        return array_list;
    }
}
