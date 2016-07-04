package com.eunovate.eunovatedev.myapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.eunovate.eunovatedev.myapp.dao_class.AttendanceDBHelper;
import com.eunovate.eunovatedev.myapp.dao_class.Class_DBHelper;
import com.eunovate.eunovatedev.myapp.dao_class.Schedule_DBHelper;
import com.eunovate.eunovatedev.myapp.dao_class.Stu_DBHelper;
import com.eunovate.eunovatedev.myapp.object.Attendance_Record_Object;
import com.eunovate.eunovatedev.myapp.object.BehaviourObj;
import com.eunovate.eunovatedev.myapp.object.ClassObject;
import com.eunovate.eunovatedev.myapp.object.ScheduleObject;
import com.eunovate.eunovatedev.myapp.object.StudentObject;
import com.eunovate.eunovatedev.myapp.object.VehicleObject;
import com.eunovate.eunovatedev.myapp.object.notiObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by EunovateDev on 1/23/2016.
 */
public class DownloadService extends IntentService {

    private int result = HomeActivity.RESULT_CANCELED;
    public static final String NOTI_DATA = "noti_data";
    public static final String SCHEDULE_DATA = "schedule_data";
    public static final String NOTIFICATION = "com.vogella.android.service.receiver";
    private Timer timer;
    private Boolean finished=false;
    public DownloadService() {
        super("DownloadService");
    }
    public Boolean noti_showed=false;
    private int intervalCount=0;
    private int user_id,urole_id;
    private String noti_status,schedule_data;
    private JSONObject jobj;
    private AttendanceDBHelper dbHelper;
    private ScheduleObject schedule_obj;
    private VehicleObject vehicle_obj;
    private JSONArray schedules,all_class,jsonArr,schedujArr,vehiclejArr,classjArr,lct_jarr,all_behaviour,all_comment;
    private JSONObject jsonObj;
    private notiObject notiObj;
    private JSONObject json_data;
    private ScheduleObject scheduleRow;
    private Schedule_DBHelper schedule_db;
    private  Stu_DBHelper stu_db;
    private ClassObject classObj;
    private Class_DBHelper class_db;
    private BehaviourObj bObj;
    private  ArrayList<notiObject> noti_arr;
    private ClassObject classRow;
    private String get_schedule_link = "";
    private ArrayList<StudentObject> new_stu_arr;

    // will be called asynchronously by Android
    // automically call every 5 seconds.

    public void myInteval(){
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                intervalCount++;
                if (intervalCount >= 5) {
                    if(MainActivity.isOnline()){

                        if (dbHelper.check_update() && urole_id == 4) {
                            Log.i("log tag", "Hv to post true ");
                            new post_schedule().execute();
                        } else {
                            Log.i("log tag", "Hvn't to post true ");
                        }
                        new get_schedule_data().execute();

                        noti_arr = dbHelper.get_post_noti_list();

                        if (noti_arr.size() > 0) {
                            new post_noti().execute();

                        }

                        new getNoti().execute();
                        intervalCount = 0;
                    }
//                    if (MainActivity.isInternetAvailable()) {
//
//                    }
                   myInteval();

                } else {
                    if(MainActivity.isInternetAvailable()) {
                        noti_arr = dbHelper.get_post_noti_list();
                        Log.i("LOG TAG", "NOTI TO POST " + noti_arr);
                        if (noti_arr.size() > 0) {
                            new post_noti().execute();
                            Log.i("LOG TAG", "POSTED NOTI ");
                        }
                        new getNoti().execute();
                    }
                    myInteval();
                }

            }
        }, 1000*300);
    }

    public class post_schedule extends AsyncTask<String,String,String> {
//        private ProgressDialog progressDialog=ProgressDialog.show(getApplicationContext(),"","Loading..");

        protected void onPreExecute(){}
        @Override
        protected String doInBackground(String... arg0){
            try {
                ArrayList<StudentObject> new_stu_arr = dbHelper.get_new_student();
                JSONArray stu_jarr = new JSONArray();

                if(new_stu_arr.size()>0) {
                    for (StudentObject sobj : new_stu_arr) {
                        jsonObj = new JSONObject();
                        jsonObj.put("student_id", sobj.getStudent_id());
                        jsonObj.put("name", sobj.getName());
                        jsonObj.put("address",sobj.getAddress());
                        jsonObj.put("gender",sobj.getGender());
                        jsonObj.put("created_date",sobj.getCreated_date());
                        jsonObj.put("active_in_sub",sobj.getActiv_in_subject());
                        jsonObj.put("reactivated_date",sobj.getReactivated_date());

                        if(sobj.getFather_name()==null){
                            jsonObj.put("father_name"," ");
                        }else
                            jsonObj.put("father_name",sobj.getFather_name());

                        if(sobj.getMother_name()==null)
                            jsonObj.put("mother_name"," ");
                        else
                            jsonObj.put("mother_name",sobj.getMother_name());

                        jsonObj.put("is_active",sobj.getIs_active());
                        jsonObj.put("stu_new",sobj.getStu_new());

                        if(sobj.getComment()==null)
                            jsonObj.put("comment","");
                        else
                            jsonObj.put("comment",sobj.getComment());


                        if(sobj.getDate_of_birth()==null){
                            jsonObj.put("date_of_birth"," ");
                        }else{
                            jsonObj.put("date_of_birth",sobj.getDate_of_birth());
                        }

                        jsonObj.put("class_id",sobj.getClass_id());
                        jsonObj.put("course_id",sobj.getCourse_id());
                        jsonObj.put("schedule_id",sobj.getSchedule_id());
                        jsonObj.put("atd_id",sobj.getAttendance());
                        jsonObj.put("gender", sobj.getGender());
                        jsonObj.put("address", sobj.getAddress());
                        jsonObj.put("present_flag",sobj.getPresent_flag());
                        jsonObj.put("behaviour",dbHelper.get_stu_atd_rcd(sobj.getAttendance()));
                        stu_jarr.put(jsonObj);
                    }
                }

                ArrayList<Attendance_Record_Object> atd_rcd_arr=new ArrayList<>();
                JSONArray jrArr=new JSONArray();
                JSONObject jobj=new JSONObject();
                JSONArray jrRcdArr = new JSONArray();
                JSONObject jrRcdObj = new JSONObject();
                atd_rcd_arr = dbHelper.get_attendance_record();

                if(atd_rcd_arr.size()>0) {
                    int atd_temp = 0;
                    Boolean first = true;
                    for (Attendance_Record_Object obj : atd_rcd_arr) {
                        if (atd_temp == obj.getAttendance_id()) {
                            jrRcdObj = new JSONObject();
                            jrRcdObj.put("behaviour_id", obj.getBehaviour_id());
                            jrRcdObj.put("rating", obj.getRating());
                            jrRcdArr.put(jrRcdObj);
                        } else if (first) {
                            jobj = new JSONObject();
                            jobj.put("attendance_id", obj.getAttendance_id());
                            jobj.put("student_id", obj.getStudent_id());
                            jobj.put("present_flag", obj.getPresent_flag());
                            jobj.put("schedule_id", obj.getSchedule_id());
                            jobj.put("comment", obj.getComment());
                            jobj.put("present_flag", obj.getPresent_flag());
                            jrRcdObj = new JSONObject();
                            jrRcdObj.put("behaviour_id", obj.getBehaviour_id());
                            jrRcdObj.put("rating", obj.getRating());
                            jrRcdArr.put(jrRcdObj);
                            first = false;
                        } else {
                            jobj.put("record", jrRcdArr);
                            jrArr.put(jobj);
                            jobj = new JSONObject();
                            jobj.put("attendance_id", obj.getAttendance_id());
                            jobj.put("student_id", obj.getStudent_id());
                            jobj.put("present_flag", obj.getPresent_flag());
                            jobj.put("schedule_id", obj.getSchedule_id());
                            jobj.put("comment", obj.getComment());
                            jobj.put("present_flag", obj.getPresent_flag());
                            jrRcdObj = new JSONObject();
                            jrRcdObj.put("behaviour_id", obj.getBehaviour_id());
                            jrRcdObj.put("rating", obj.getRating());
                            jrRcdArr = new JSONArray();
                            jrRcdArr.put(jrRcdObj);
                        }
                        atd_temp = obj.getAttendance_id();
                    }
                    jobj.put("record", jrRcdArr);
                    jrArr.put(jobj);
                }

                jobj=new JSONObject();
                jobj.put("attendance",jrArr);
                jobj.put("student",stu_jarr);

                String link=HomeActivity.BASE_URL+"post_attendance";
                String data=jobj.toString();
                //String data="";
                URL url=new URL(link);
                URLConnection conn=url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb=new StringBuffer();
                String line=null;
                while ((line=reader.readLine())!=null){
                    sb.append(line);
                }
                return sb.toString();
            }
            catch (Exception e){
                return new String("Exception: "+e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result){
            Log.i("LOG TAG","Attendance Post return result : "+result);
            try {
                jsonObj=new JSONObject(result);
                if(jsonObj.getBoolean("atd_res")){
                    dbHelper.finished_post();
                    Toast.makeText(getApplicationContext(), "Attendance has been posted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Can't post attendance. Try again!", Toast.LENGTH_SHORT).show();
                }
                if(jsonObj.getBoolean("stu_res")){
                    Toast.makeText(getApplicationContext(), "Student has been posted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Can't post student. Try again", Toast.LENGTH_SHORT).show();
                }
                if(!jsonObj.getBoolean("stu_cls_res")){
                    Toast.makeText(getApplicationContext(), "Class has been posted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Can't post class. Try again", Toast.LENGTH_SHORT).show();
                }

            }

            catch (Exception e){
                Log.e("LOG TAG ","Exception"+e.getMessage());
                //return new String("Exception: "+e.getMessage());
            }
            //progressDialog.dismiss();
        }
    }

    private class post_noti extends AsyncTask<String,String,String> {
        protected void onPreExecute() {}
        @Override
        protected String doInBackground(String... arg0) {
            try {
                JSONArray jrArr = new JSONArray();
                for (notiObject nobj : noti_arr){
                    jobj = new JSONObject();
                    jobj.put("noti_id",nobj.getNoti_id());
                    jrArr.put(jobj);
                }
                jobj = new JSONObject();
                jobj.put("noti_arr",jrArr);
                jobj.put("user_id",user_id);
                String link = HomeActivity.BASE_URL + "post_noti";
                String data = jobj.toString();
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result){
            Log.i("LOG TAG", "POSTED NOTI RESULT  " +result);
            if (result.equals("true")) {
                if(dbHelper.sent_noti()) {
                    Log.i("LOG_TAG","FINISHED SYNC NOTI");
                }
            }
            else {
                Log.i("LOG_TAG","CAN'T POST SYNC POST");
            }
        }
    }

    @Override
    public void onCreate(){

        super.onCreate();
        dbHelper=new AttendanceDBHelper(this);
        SharedPreferences pref=getSharedPreferences("loginPrefs", 0);
        user_id=pref.getInt("user_id",0);
        urole_id=pref.getInt("role",0);
        if(urole_id==4)
            get_schedule_link="getschedulelst";
        else
            get_schedule_link="getdriverdata";

        noti_arr=new ArrayList<>();
        schedule_db = new Schedule_DBHelper(this);
        stu_db=new Stu_DBHelper(this);
        class_db=new Class_DBHelper(this);

    }

    public void save_noti_data(String data){
        Log.i("LOGTAG","noti string"+data);
        try{
            noti_arr=new ArrayList<>();
            //Get Noti array from Downloadservice class
            jsonArr=new JSONArray(data);

            if (dbHelper.clear_noti_data()) {
                for (int i = 0; i < jsonArr.length(); i++) {
                    jsonObj = jsonArr.getJSONObject(i);
                    notiObj = new notiObject();
                    notiObj.setNoti_id(jsonObj.getInt("noti_id"));
                    notiObj.setDescription(jsonObj.getString("noti_description"));
                    String dtStart = jsonObj.getString("created_date");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date date = format.parse(dtStart);
                        notiObj.setDate(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    notiObj.setSeen(jsonObj.getInt("seen"));
                    // Insert notification to db
                    dbHelper.insert_noti(notiObj, jsonObj.getInt("user_id"));

                }
            }

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data" + e.toString());
        }
    }

    public void save_driver_schedule(String data){

        SimpleDateFormat date_time_fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date_fmt = new SimpleDateFormat("yyyy-MM-dd");
        // get schedule data and insert
        try {
            jsonObj= new JSONObject(data);
            schedujArr=jsonObj.getJSONArray("schedule");
            vehiclejArr=jsonObj.getJSONArray("cars");
            classjArr=jsonObj.getJSONArray("class");
            lct_jarr=jsonObj.getJSONArray("vlocation");

            // get schedule data and insert
            if(schedujArr.length()>0) {
                dbHelper.clear_schedule();
                dbHelper.clear_location();
                for (int i = 0; i < schedujArr.length(); i++) {
                    jsonObj = new JSONObject();
                    schedule_obj = new ScheduleObject();
                    jsonObj = schedujArr.getJSONObject(i);
                    schedule_obj.setSchedule_id(jsonObj.getInt("schedule_id"));
                    String schedule_date = jsonObj.getString("schedule_date");
                    String start_time = jsonObj.getString("start_time");
                    String end_time = jsonObj.getString("end_time");

                    try {
                        schedule_obj.setDate(date_fmt.parse(schedule_date));
                        schedule_obj.setStart_time(date_time_fmt.parse(start_time));
                        schedule_obj.setEnd_time(date_time_fmt.parse(end_time));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(jsonObj.getInt("teacherid")<1 )
                        schedule_obj.setTeacher(" ");
                    else
                        schedule_obj.setTeacher(jsonObj.getString("teachername"));

                    schedule_obj.setClass_id(jsonObj.getInt("class_id"));
                    schedule_obj.setDriver(jsonObj.getString("drivername"));
                    schedule_obj.setDriver_id(jsonObj.getInt("driver_id"));
                    schedule_obj.setVehicle(jsonObj.getString("vehiclename"));
                    schedule_obj.setLocation(jsonObj.getString("location_desc"));
                    schedule_obj.setLocation_id(jsonObj.getInt("location_id"));

                    dbHelper.insert_driver_schedule(schedule_obj);
                }
                Log.i("log_tag", "Inserted schedule!");
            }

            if(classjArr.length()>0) {
                dbHelper.clear_class();
                dbHelper.clear_student();
                for (int i = 0; i < classjArr.length(); i++) {
                    jsonObj = classjArr.getJSONObject(i);
                    classObj = new ClassObject();
                    classObj.setClass_id(jsonObj.getInt("class_id"));
                    classObj.setClassname(jsonObj.getString("class_name"));
                    classObj.setLocation(jsonObj.getString("location_desc"));
                    classObj.setVehicle(jsonObj.getString("vehiclename"));
                    classObj.setDivision(" ");
                    classObj.setCity(" ");
                    classObj.setAddress(" ");
                    classObj.setPhone(" ");
                    classObj.setStart_date(null);
                    classObj.setEnd_date(null);
                    classObj.setTeacher_id(0);
                    classObj.setTeacher_id(jsonObj.getInt("userid"));
                    class_db.insert(classObj);
                }
                Log.i("log_tag", "Inserted class!");
            }

            if(vehiclejArr.length()>0){
                //get vehicle data and insert
                dbHelper.clear_vehicle();
                for (int i=0; i<vehiclejArr.length(); i++){
                    jsonObj = new JSONObject();
                    vehicle_obj= new VehicleObject();
                    jsonObj=vehiclejArr.getJSONObject(i);
                    vehicle_obj.setVehicle_id(jsonObj.getInt("vehicle_id"));
                    vehicle_obj.setVehicle_no(jsonObj.getString("vehiclename"));
                    vehicle_obj.setVehicle_model(jsonObj.getString("v_model"));
                    dbHelper.insert_vehicle(vehicle_obj);
                }
                Log.i("log_tag", "Inserted vehicle!");
            }

            if(lct_jarr.length()>0) {
                dbHelper.clear_location();
                for (int i = 0; i < lct_jarr.length(); i++) {
                    dbHelper.insert_location(lct_jarr.getJSONObject(i));
                }
                Log.i("log_tag", "Inserted vehicle location!");
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data" + e.toString());
        }
    }

    public void save_schedule_data(String data){

        SimpleDateFormat date_time_fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date_fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            jsonObj = new JSONObject(data);
            schedules = jsonObj.getJSONArray("schedule");
            // students = jsonObj.getJSONArray("student");
            all_class = jsonObj.getJSONArray("class");
            all_behaviour=jsonObj.getJSONArray("behaviour");
            all_comment=jsonObj.getJSONArray("comments");
            dbHelper.clear_schedule();

            for (int i = 0; i < schedules.length(); i++) {
                json_data = schedules.getJSONObject(i);
                scheduleRow = new ScheduleObject();
                scheduleRow.setSchedule_id(json_data.getInt("schedule_id"));
                scheduleRow.setCourse(json_data.getString("coursename"));
                scheduleRow.setLesson(json_data.getString("lessonname"));
                scheduleRow.setLevel(json_data.getString("level_name"));
                String strDate = json_data.getString("schedule_date");
                String strSDate = json_data.getString("start_time");
                String strEDate = json_data.getString("end_time");
                try {
                    scheduleRow.setDate(date_fmt.parse(strDate));
                    scheduleRow.setStart_time(date_time_fmt.parse(strSDate));
                    scheduleRow.setEnd_time(date_time_fmt.parse(strEDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                scheduleRow.setTeacher(json_data.getString("teachername"));

                if(json_data.getInt("vehicle_id")>0){
                    scheduleRow.setDriver(json_data.getString("drivername"));
                    scheduleRow.setVehicle(json_data.getString("vehiclename"));
                }
                else if(json_data.getInt("vehicle_id")==0){
                    scheduleRow.setDriver(" ");
                    scheduleRow.setVehicle("Taxi");
                }
                else{
                    scheduleRow.setDriver(" ");
                    scheduleRow.setVehicle("Other");
                }
                scheduleRow.setTeacher_id(json_data.getInt("teacherid"));
                scheduleRow.setDriver_id(json_data.getInt("driver_id"));

                scheduleRow.setClass_id(json_data.getInt("class_id"));
                scheduleRow.setCourse_id(json_data.getInt("course_id"));
                scheduleRow.setRecorded(json_data.getInt("recorded"));
                schedule_db.insertSchedule(scheduleRow);
            }
            Log.i("log_tag", "Inserted schedule!");

            dbHelper.clear_class();
            dbHelper.clear_student();

            for (int i = 0; i < all_class.length(); i++) {
                jobj = all_class.getJSONObject(i);
                classRow = new ClassObject();
                classRow.setClass_id(jobj.getInt("class_id"));
                classRow.setClassname(jobj.getString("class_name"));
                classRow.setLocation(jobj.getString("location_desc"));
                classRow.setDivision(jobj.getString("division_name"));
                classRow.setCity(jobj.getString("city_name"));
                classRow.setAddress(jobj.getString("contact_address"));
                classRow.setPhone(jobj.getString("contact_phone"));
                String strSDate = jobj.getString("start_date");
                String strEDate = jobj.getString("end_date");
                try {
                    classRow.setStart_date(date_fmt.parse(strSDate));
                    classRow.setEnd_date(date_fmt.parse(strEDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(json_data.getInt("vehicle_id")>0){
                    classRow.setVehicle(jobj.getString("vehiclename"));
                }
                else if(json_data.getInt("vehicle_id")==0){
                    classRow.setVehicle("Taxi");
                }
                else{
                    classRow.setVehicle("Other");

                }

                classRow.setTeacher_id(jobj.getInt("userid"));
                JSONArray classStudents = jobj.getJSONArray("students");
                class_db.insert_class_student(classStudents, jobj.getInt("class_id"),jobj.getInt("course_id"));
                class_db.insert(classRow);
            }
            Log.i("log_tag", "Inserted class & student!");

            if(all_behaviour.length()>0) {
                dbHelper.clear_behaviour();
                for (int i = 0; i < all_behaviour.length(); i++) {
                    jsonObj = all_behaviour.getJSONObject(i);
                    bObj = new BehaviourObj();
                    bObj.setBehaviourID(jsonObj.getInt("behaviour_id"));
                    bObj.setBehaviour(jsonObj.getString("description"));
                    dbHelper.insert_behaviour(bObj);

                }
                Log.i("log_tag", "Inserted behaviour!");
            }

            if(all_comment.length()>0) {
                dbHelper.clear_comment();
                for (int i = 0; i < all_comment.length(); i++) {
                    dbHelper.insert_comment(all_comment.getJSONObject(i));
                }
                Log.i("log_tag", "Inserted comment!");
            }

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data" + e.toString());
        }
    }

    // Get noti from server
    private class get_schedule_data extends AsyncTask<String,String,String> {

        protected void onPreExecute(){}
        @Override
        protected String doInBackground(String... arg0){
            try {

                String data= URLEncoder.encode("user_id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(user_id),"UTF-8");
                String link = HomeActivity.BASE_URL + get_schedule_link;
                URL url = new URL(link);
                URLConnection conn=url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb=new StringBuffer();
                //StringBuilder result=new StringBuilder();
                String line=null;
                //Read Server Response
                while ((line=reader.readLine())!=null){
                    sb.append(line);
                }
                return sb.toString();
            }
            catch (Exception e){
                return new String("Exception: "+e.getMessage());

            }
        }

        @Override
        protected void onPostExecute(String result){
            Log.i("LOG TAG","GET SCHEDULE RESULT " + result);

            if(!result.equals("false")) {
               // publishResults(null,result);
               // schedule_data=result;
                if(urole_id==4)
                    save_schedule_data(result);
                else
                    save_driver_schedule(result);

                publishResults();
            }
        }
    }

    // Get noti from server
    private class getNoti extends AsyncTask<String,String,String> {

        SharedPreferences pref=getSharedPreferences("loginPrefs", 0);
        int user_id=pref.getInt("user_id",0);
        protected void onPreExecute(){}
        @Override
        protected String doInBackground(String... arg0){
            try {

                String data= URLEncoder.encode("user_id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(user_id),"UTF-8");
                String link = HomeActivity.BASE_URL+"get_noti_list";
                URL url = new URL(link);
                URLConnection conn=url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb=new StringBuffer();
                //StringBuilder result=new StringBuilder();
                String line=null;
                //Read Server Response
                while ((line=reader.readLine())!=null){
                    sb.append(line);
                }
                return sb.toString();
            }
            catch (Exception e){
                return new String("Exception: "+e.getMessage());

            }
        }

        @Override
        protected void onPostExecute(String result){
        Log.i("Log_Tag","GET NOTI DATA"+result);
        try {
                    JSONObject jsonObj = new JSONObject(result);
                    if(jsonObj.getBoolean("success")){
                        save_noti_data(jsonObj.getJSONArray("noti_arr").toString());
                        //publishResults(jsonObj.getJSONArray("noti_arr").toString(),schedule_data);
                        set_noti(jsonObj.getJSONArray("noti_arr"));
                        publishResults();
                    }else{
                        dbHelper.clear_noti_data();
                        publishResults();
                    }

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        }
    }

    // Set notification to app.
    public  void set_noti(JSONArray array){

        try {
            jobj=new JSONObject();
            if(array.length()>1){
                noti_status="You have " +array.length() + " notification !";
            }else{
                jobj=array.getJSONObject(0);
                noti_status = " "+jobj.getString("noti_description");
            }

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data" + e.toString());

        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("myME")
                        .setContentText(noti_status);

        mBuilder.setAutoCancel(true);
        Intent resultIntent = new Intent(this, Noti_Activity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());
    }

    protected void onHandleIntent(Intent intent) {
        String noti_data = intent.getStringExtra(NOTI_DATA);
        myInteval();
    }

    private void publishResults() {
        Log.i("LOG TAG ", "LOADED publichResults method");
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(NOTI_DATA, true);
        //intent.putExtra(SCHEDULE_DATA, schedule_data);
        sendBroadcast(intent);
    }
}
