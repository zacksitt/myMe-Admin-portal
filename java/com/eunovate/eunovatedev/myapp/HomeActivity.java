package com.eunovate.eunovatedev.myapp;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;
import com.eunovate.eunovatedev.myapp.dao_class.AttendanceDBHelper;
import com.eunovate.eunovatedev.myapp.dao_class.Class_DBHelper;
import com.eunovate.eunovatedev.myapp.dao_class.DataBaseHelper;
import com.eunovate.eunovatedev.myapp.dao_class.Schedule_DBHelper;
import com.eunovate.eunovatedev.myapp.dao_class.Stu_DBHelper;
import com.eunovate.eunovatedev.myapp.object.Attendance_Record_Object;
import com.eunovate.eunovatedev.myapp.object.BehaviourObj;
import com.eunovate.eunovatedev.myapp.object.ClassObject;
import com.eunovate.eunovatedev.myapp.object.ScheduleObject;
import com.eunovate.eunovatedev.myapp.object.StudentObject;
import com.eunovate.eunovatedev.myapp.object.notiObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity  {

    // Online Server API Link
    public static final String BASE_URL="http://128.199.208.42/myme/service/index.php/Schedule_output_ctrl/";
    // Geny Emulator API Link
    // public static final String BASE_URL="http://10.0.3.2/eunovate/ams/service/index.php/Schedule_output_ctrl/";
    // Android Emulator API Link
    // public static final String BASE_URL="http://10.0.2.2/eunovate/ams/service/index.php/Schedule_output_ctrl/";

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;

    int Numboftabs =2;
    ArrayList<ScheduleObject> arrayOfWebData=new ArrayList<ScheduleObject>();
    public  static final String MyPREFERENCES="MyPrefs";
    public  static final String KEY_TOKEN="token";
    private static final int PREFERENCE_MODE_PRIVATE=0;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Schedule_DBHelper schedule_db;
    private Stu_DBHelper stu_db;
    private Class_DBHelper class_db;
    public static int urole_id,user_id;
    private notiObject notiObj;
    private BehaviourObj bObj;
    private AttendanceDBHelper atd_db;
    private String token;
    private ArrayList<notiObject> noti_arr;
    private JSONObject json_data,stuJObj,classObj;
    private JSONArray schedules,all_comment,all_behaviour,all_class,jsonArr;
    private JSONObject jsonObj;
    private ScheduleObject scheduleRow;
    private ClassObject classRow;
    private ArrayList<String> menuList;
    private DataBaseHelper dbHelper;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private CharSequence Titles[];
    private int back_click=0;
    private SimpleDateFormat date_fmt,datetime_fmt,time_fmt;

    //Tempoary CODE
    private int year,month,day;
    private DatePicker datePicker;
    private Calendar calendar;
    private String retrive_date;
    /////// END TEMP CODE////////////

    //Receive post from background service (DownloadService.java)
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                set_tabs();
            }
        }
    };

//    public void save_noti_data(String data){
//        try{
//            noti_arr=new ArrayList<>();
//            //Get Noti array from Downloadservice class
//            jsonArr=new JSONArray(data);
//
//            if (atd_db.clear_noti_data()) {
//                for (int i = 0; i < jsonArr.length(); i++) {
//                    jsonObj = jsonArr.getJSONObject(i);
//                    notiObj = new notiObject();
//                    notiObj.setNoti_id(jsonObj.getInt("noti_id"));
//                    notiObj.setDescription(jsonObj.getString("noti_description"));
//                    String dtStart = jsonObj.getString("created_date");
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    try {
//                        Date date = format.parse(dtStart);
//                        notiObj.setDate(date);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    notiObj.setSeen(jsonObj.getInt("seen"));
//                    // Insert notification to db
//                    atd_db.insert_noti(notiObj, jsonObj.getInt("user_id"));
//
//                }
//                set_tabs();
//            }
//
//        } catch (JSONException e) {
//            //Log.e("log_tag", "Error parsing data" + e.toString());
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dbHelper=new DataBaseHelper(this);
        schedule_db = new Schedule_DBHelper(this);
        stu_db=new Stu_DBHelper(this);
        class_db=new Class_DBHelper(this);
        atd_db=new AttendanceDBHelper(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        date_fmt=new SimpleDateFormat("yyyy-MM-dd");
        datetime_fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time_fmt=new SimpleDateFormat("HH:mm a");

        ////////// TEMP CODE //////////////////
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        ////////////// END TEMP CODE ///////////////

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);

        pref=getSharedPreferences("loginPrefs",PREFERENCE_MODE_PRIVATE);
        token=pref.getString("token", null);
        user_id=pref.getInt("user_id", 0);
        set_tabs();

            //Get Drawer(side bar) list from xml
        mDrawerList = (ListView)findViewById(R.id.navList);
        //Get Drawer Layout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        addDrawerItems();
        setupDrawer();

        Bundle extras=getIntent().getExtras();

        if(extras!=null){
            int id = extras.getInt("atd_finished_param");
            new post_schedule().execute();
        }
    }

    // Temp code
    @Override
    protected Dialog onCreateDialog(int id) {

        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            String dd,mm;
            arg2 = arg2 +1;
            if(arg3<10){
                dd= "0" + Integer.toString(arg3);
            }
            else{
                dd = Integer.toString(arg3);
            }
            if(arg2 < 10){
                mm= "0" + Integer.toString(arg2);
            }
            else{
                mm = Integer.toString(arg2);
            }
            String yy = Integer.toString(arg1);
            retrive_date = yy + "-" + mm + "-" + dd ;
            if(MainActivity.isOnline()){
                if (atd_db.check_update()) {
                    // if(atd_db.check_update() || atd_db.check_stu_update()) {
                    new post_schedule().execute();
                } else {
                    Toast.makeText(HomeActivity.this, "There is no schedule to post !", Toast.LENGTH_SHORT).show();
                }
                new getData().execute();
                getWindow().setWindowAnimations(0);

            }else{
                Toast.makeText(HomeActivity.this, "No internet connection !", Toast.LENGTH_SHORT).show();
            }
            //showDate(arg1, arg2+1, arg3);
        }
    };
    ///////////////////// Temp COde END /////////////

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(999);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        back_click++;
        if(keyCode == KeyEvent.KEYCODE_BACK && back_click==1){
            Toast.makeText(HomeActivity.this, "Press back again to exit! ", Toast.LENGTH_SHORT).show();
            return false;
        }else if (keyCode == KeyEvent.KEYCODE_BACK && back_click > 1) {
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void set_tabs(){
        // Get adapter to set tab to Activity
        Titles=new CharSequence[2];
        Titles[0]="Schedule";
        int noti_count=atd_db.get_noti_count();
        if(noti_count>0)
            Titles[1]="Notification (" + String.valueOf(noti_count) + ")";
        else
            Titles[1]="Notification";

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {

                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              //  Log.i("LOG TAG","PageScroll "+position);
            }
            @Override
            public void onPageSelected(int position) {
                if(position>0){
                   atd_db.seen_noti();
                }else{
                    set_tabs();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
               // Log.i("LOG TAG","PageScroll state changed"+state);
            }
        });
    }

    public void closeDrawer(){
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    public void openDrawer(){ mDrawerLayout.openDrawer(mDrawerList); }
    private void addDrawerItems() {
        ScheduleObject s=new ScheduleObject();
        urole_id=pref.getInt("role", 0);
        menuList=new ArrayList<String>();
        menuList.add("About");
        menuList.add("Setting");
        if(token!=null && urole_id==1){
            menuList.add("Driver mode");
        }
        menuList.add("Logout");
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeDrawer();

                if (urole_id > 1 && position == 2) {
                    position = 3;
                }
                if (position == 3) {
                    //Vehicle Usage
                    pref = getSharedPreferences("loginPrefs", PREFERENCE_MODE_PRIVATE);
                    editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    finish();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);

                } else if (position == 2) {
                    Intent intent = new Intent(HomeActivity.this, DriverHome_Activity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu();
                // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerClosed(View view) {

                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
                 // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(DownloadService.NOTIFICATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public class post_schedule extends AsyncTask<String,String,String> {
        private ProgressDialog progressDialog=ProgressDialog.show(HomeActivity.this,"","Loading..");

        protected void onPreExecute(){}
        @Override
        protected String doInBackground(String... arg0){
            try {
                ArrayList<StudentObject> new_stu_arr=atd_db.get_new_student();
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
                        // Student stu_new value 1 for new student,2 for activate student,3 for activate student in subject
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
                        jsonObj.put("behaviour",atd_db.get_stu_atd_rcd(sobj.getAttendance()));
                        stu_jarr.put(jsonObj);
                    }
                }

                ArrayList<Attendance_Record_Object> atd_rcd_arr=new ArrayList<>();
                JSONArray jrArr=new JSONArray();
                JSONObject jobj=new JSONObject();
                JSONArray jrRcdArr = new JSONArray();
                JSONObject jrRcdObj = new JSONObject();
                atd_rcd_arr = atd_db.get_attendance_record();

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
                            jobj.put("present_flag",obj.getPresent_flag());
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
                            jobj.put("present_flag", obj.getPresent_flag());
                            jobj.put("comment", obj.getComment());
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

                String link=BASE_URL+"post_attendance";
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
            try {
                    jsonObj = new JSONObject(result);
                    if (jsonObj.getBoolean("atd_res")) {
                        atd_db.finished_post();
                        Toast.makeText(HomeActivity.this, "Attendance has been posted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HomeActivity.this, "Can't post attendance. Try again!", Toast.LENGTH_SHORT).show();
                    }
                    if (jsonObj.getBoolean("stu_res")) {
                        Toast.makeText(HomeActivity.this, "Student has been posted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HomeActivity.this, "Can't post student. Try again", Toast.LENGTH_SHORT).show();
                    }
                    if (!jsonObj.getBoolean("stu_cls_res")) {
                        Toast.makeText(HomeActivity.this, "Class has been posted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HomeActivity.this, "Can't post class. Try again", Toast.LENGTH_SHORT).show();
                    }

            }

            catch (Exception e){
//                /Log.e("LOG TAG ","Exception"+e.getMessage());
                //return new String("Exception: "+e.getMessage());
            }
            progressDialog.dismiss();
        }
    }

    private class getData extends AsyncTask<String,String,String> {

        private ProgressDialog progressDialog=ProgressDialog.show(HomeActivity.this,"","Loading..");
        protected void onPreExecute(){}
        @Override
        protected String doInBackground(String... arg0){
            try {
                //int last_sync_count=atd_db.get_sync_schedule_count();
                String link=BASE_URL+"getschedulelst";
                //String data= URLEncoder.encode("user_id", "UTF-8")+" = "+ URLEncoder.encode(String.valueOf(user_id),"UTF-8");
                /// TEMP CODE //////////////
                JSONObject jobj= new JSONObject();
                jobj.put("user_id",user_id);
                jobj.put("date",retrive_date);
                String data = jobj.toString();
                //// END TEMP CODE ///////////////
                URL url=new URL(link);
                URLConnection conn=url.openConnection();
                conn.setDoOutput(true);
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
            Log.i("LOG TAG","GET DATA RESULT : "+result);
            if(!result.equals("false")) {
                if(save_schedule_data(result)){
                   progressDialog.dismiss();
                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                };

            }else{
                Toast.makeText(HomeActivity.this, "There is no schedule to update !", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }

    public Boolean save_schedule_data(String data){

        try {
            jsonObj = new JSONObject(data);
            schedules = jsonObj.getJSONArray("schedule");
            all_class = jsonObj.getJSONArray("class");
            all_behaviour=jsonObj.getJSONArray("behaviour");
            all_comment=jsonObj.getJSONArray("comments");

            if(schedules.length()>0) {
                atd_db.clear_schedule();
                for (int i = 0; i < schedules.length(); i++) {
                    json_data = schedules.getJSONObject(i);
                    scheduleRow = new ScheduleObject();
                    scheduleRow.setSchedule_id(json_data.getInt("schedule_id"));
                    scheduleRow.setCourse(json_data.getString("coursename"));
                    scheduleRow.setLesson(json_data.getString("lessonname"));
                    scheduleRow.setCourse_id(json_data.getInt("course_id"));
                    scheduleRow.setLevel(json_data.getString("level_name"));
                    String strDate = json_data.getString("schedule_date");
                    String strSDate = json_data.getString("start_time");
                    String strEDate = json_data.getString("end_time");

                    try {

                        scheduleRow.setDate(date_fmt.parse(strDate));
                        scheduleRow.setStart_time(datetime_fmt.parse(strSDate));
                        scheduleRow.setEnd_time(datetime_fmt.parse(strEDate));


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
                    scheduleRow.setRecorded(json_data.getInt("recorded"));
                    schedule_db.insertSchedule(scheduleRow);
                }
                    Toast.makeText(HomeActivity.this, "Inserted schedule!", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(HomeActivity.this, "No schedule !", Toast.LENGTH_SHORT).show();

            }

            if(all_class.length()>0) {
                atd_db.clear_class();
                atd_db.clear_student();
                for (int i = 0; i < all_class.length(); i++) {
                    classObj = all_class.getJSONObject(i);
                    classRow = new ClassObject();
                    classRow.setClass_id(classObj.getInt("class_id"));
                    classRow.setClassname(classObj.getString("class_name"));
                    classRow.setLocation(classObj.getString("location_desc"));
                    classRow.setDivision(classObj.getString("division_name"));
                    classRow.setCity(classObj.getString("city_name"));
                    classRow.setAddress(classObj.getString("contact_address"));
                    classRow.setCourse_id(classObj.getInt("course_id"));
                    classRow.setPhone(classObj.getString("contact_phone"));

                    String strSDate = classObj.getString("start_date");
                    String strEDate = classObj.getString("end_date");

                    try {
                        classRow.setStart_date(date_fmt.parse(strSDate));
                        classRow.setEnd_date(date_fmt.parse(strEDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(json_data.getInt("vehicle_id")>0){
                        classRow.setVehicle(classObj.getString("vehiclename"));
                    }
                    else if(json_data.getInt("vehicle_id")==0){
                        classRow.setVehicle("Taxi");
                    }
                    else{
                        classRow.setVehicle("Other");

                    }

                    classRow.setTeacher_id(classObj.getInt("userid"));
                    JSONArray classStudents = classObj.getJSONArray("students");
                    class_db.insert_class_student(classStudents, classObj.getInt("class_id"),classObj.getInt("course_id"));
                    class_db.insert(classRow);
                }
                Toast.makeText(HomeActivity.this, "Inserted class and student!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(HomeActivity.this, "No class and student!", Toast.LENGTH_SHORT).show();
            }

            if(all_behaviour.length()>0) {
                atd_db.clear_behaviour();
                for (int i = 0; i < all_behaviour.length(); i++) {
                    jsonObj = all_behaviour.getJSONObject(i);
                    bObj = new BehaviourObj();
                    bObj.setBehaviourID(jsonObj.getInt("behaviour_id"));
                    bObj.setBehaviour(jsonObj.getString("description"));
                    atd_db.insert_behaviour(bObj);
                }
                Toast.makeText(HomeActivity.this, "Inserted behaviour!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(HomeActivity.this, "No behaviour!", Toast.LENGTH_SHORT).show();
            }



            if(all_comment.length()>0) {
                atd_db.clear_comment();
                for (int i = 0; i < all_comment.length(); i++) {
                    atd_db.insert_comment(all_comment.getJSONObject(i));
                }
                Toast.makeText(HomeActivity.this, "Inserted comment!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(HomeActivity.this, "No comment!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data" + e.toString());
        }



        return true;
    }

                @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                getMenuInflater().inflate(R.menu.home_menu, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.refresh:
                          setDate();
//                        if(MainActivity.isOnline()){
//                            if (atd_db.check_update()) {
//                                // if(atd_db.check_update() || atd_db.check_stu_update()) {
//                                new post_schedule().execute();
//                            } else {
//                                Toast.makeText(HomeActivity.this, "There is no schedule to post !", Toast.LENGTH_SHORT).show();
//                            }
//                            new getData().execute();
//
//                        }else{
//                            Toast.makeText(HomeActivity.this, "No internet connection !", Toast.LENGTH_SHORT).show();
//                        }

                return super.onOptionsItemSelected(item);
            case R.id.expanded_menu:
                openDrawer();
                return super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
