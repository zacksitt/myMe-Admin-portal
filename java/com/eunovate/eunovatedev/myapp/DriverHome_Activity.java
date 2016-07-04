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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.eunovate.eunovatedev.myapp.object.Attendance_Record_Object;
import com.eunovate.eunovatedev.myapp.object.ClassObject;
import com.eunovate.eunovatedev.myapp.object.MaintenanceObject;
import com.eunovate.eunovatedev.myapp.object.ScheduleObject;
import com.eunovate.eunovatedev.myapp.object.VehicleObject;
import com.eunovate.eunovatedev.myapp.object.VehicleUsageObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DriverHome_Activity extends AppCompatActivity {
    // Online Server API Link
    public static final String BASE_URL="http://128.199.208.42/myme/service/index.php/Schedule_output_ctrl/";
    // Geny Emulator API Link
    //public static final String BASE_URL="http://10.0.3.2/eunovate/ams/service/index.php/Schedule_output_ctrl/";
    // Android Emulator API Link
    //public static final String BASE_URL="http://10.0.2.2/eunovate/ams/service/index.php/Schedule_output_ctrl/";

    Toolbar toolbar;
    ViewPager pager;
    DivHome_Page_Adapter adapter;
    SlidingTabLayout tabs;
    //Tab Array
    private CharSequence Titles[];
    int Numboftabs =2;
    private ArrayList<ScheduleObject> arrayOfWebData=new ArrayList<ScheduleObject>();
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<String> menuList;
    private String mActivityTitle;
    private static final int PREFERENCE_MODE_PRIVATE=0;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int urole_id,user_id;
    private AttendanceDBHelper atd_db;
    private JSONObject vusage_jobj,vmtn_jobj;
    private JSONObject vusage_line_jobj,dvrdata_jObj,jsonObj;
    private JSONArray vusage_jArr,vmtn_jarr,schedujArr,vehiclejArr,lct_jarr;
    private JSONArray vusage_lineArr,classjArr;
    private int back_click=0;
    private ScheduleObject schedule_obj;
    private VehicleObject vehicle_obj;
    private ClassObject classObj;
    private Class_DBHelper class_db;

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

    public void set_tabs(){

        Titles=new CharSequence[2];
        Titles[0]="Schedule";
        int noti_count=atd_db.get_noti_count();
        if(noti_count>0)
            Titles[1]="Notification (" + String.valueOf(noti_count) + ")";
        else
            Titles[1]="Notification";

        adapter =  new DivHome_Page_Adapter(getSupportFragmentManager(), Titles, Numboftabs);
        pager = (ViewPager) findViewById(R.id.pager);


        //set adapter to pager
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
        //Get Drawer (sidebar) id
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //  Log.i("LOG TAG","PageScroll "+position);
            }

            @Override
            public void onPageSelected(int position) {
                if(position>0){
                    Log.i("LOG_TAG","Noti page selected ");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Get adapter to set tab to Activity

        ////////// TEMP CODE //////////////////
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        ////////////// END TEMP CODE ///////////////

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);

        atd_db=new AttendanceDBHelper(this);
        class_db=new Class_DBHelper(this);

        set_tabs();
        mDrawerList = (ListView)findViewById(R.id.navList);
        // Get Drawer Layout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        addDrawerItems();
        setupDrawer();

        pref=getSharedPreferences("loginPrefs", PREFERENCE_MODE_PRIVATE);
        user_id=pref.getInt("user_id", 0);

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
            // TODO Auto-generated method stub
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
            Log.i("LOG TAG","DATE " +retrive_date);

            if(MainActivity.isOnline()){
                    if (atd_db.check_update_driver_data()) {
                        // if(atd_db.check_update() || atd_db.check_stu_update()) {
                        new post_driver_data().execute();
                    } else {
                        Toast.makeText(DriverHome_Activity.this, "There is no schedule to post !", Toast.LENGTH_SHORT).show();
                    }
                    new getData().execute();
                }else{
                    Toast.makeText(DriverHome_Activity.this, "No internet connection !", Toast.LENGTH_SHORT).show();
            }
            //showDate(arg1, arg2+1, arg3);
        }
    };
    ///////////////////// Temp COde END /////////////

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(999);

//        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
//                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        back_click++;
        if(back_click==1){
            Toast.makeText(DriverHome_Activity.this, "Press back again to exit! ", Toast.LENGTH_SHORT).show();
            return false;
        }else if (keyCode == KeyEvent.KEYCODE_BACK && back_click > 1) {
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

    private class post_driver_data extends AsyncTask<String,String,String> {
       // private ProgressDialog progressDialog=ProgressDialog.show(DriverHome_Activity.this,"","Loading..");
        protected void onPreExecute(){}
        @Override
        protected String doInBackground(String... arg0){
            try {
                ArrayList<VehicleUsageObject> vechicle_usg_arr=new ArrayList<>();
                ArrayList<MaintenanceObject> mtn_arr=new ArrayList<>();

                vechicle_usg_arr = atd_db.get_vehicle_usage();
                mtn_arr=atd_db.get_vehicle_mtn();
                vusage_jArr = new JSONArray();
                vmtn_jarr=new JSONArray();

                if(vechicle_usg_arr.size()>0) {

                    int atd_temp = 0;
                    Boolean first = true;
                    for (VehicleUsageObject obj : vechicle_usg_arr) {
                        vusage_jobj = new JSONObject();
                        vusage_jobj.put("vehicle_id", obj.getVehicle_id());
                        vusage_jobj.put("start_odometer", obj.getStart_odometer());
                        vusage_jobj.put("start_time", obj.getStart_time());
                        vusage_jobj.put("end_odometer", obj.getEnd_odometer());
                        vusage_jobj.put("end_time", obj.getEnd_tiem());
                        vusage_jobj.put("user_id", user_id);
                        vusage_lineArr = atd_db.get_usage_line(obj.getVehicle_usage_id());

                        Log.i("LOG TAG","USAGE ID : " +obj.getVehicle_usage_id());
                        Log.i("LOG TAG","USAGE LINE : "+vusage_lineArr);

                        vusage_jobj.put("usage_lines", vusage_lineArr);
                        vusage_jArr.put(vusage_jobj);
                    }
                }

                if(mtn_arr.size()>0){
                    for (MaintenanceObject o : mtn_arr) {
                        vmtn_jobj = new JSONObject();
                        vmtn_jobj.put("vehicle_id", o.getVehicle());
                        vmtn_jobj.put("oil",o.getOil());
                        vmtn_jobj.put("coolant",o.getCoolant());
                        vmtn_jobj.put("aircon",o.getAir());
                        vmtn_jobj.put("engine_oil",o.getEngineoile());
                        vmtn_jobj.put("car_body",o.getCar_body());
                        vmtn_jobj.put("brake",o.getBrake());
                        vmtn_jobj.put("light",o.getLight());
                        vmtn_jobj.put("fb_light",o.getFb_light());
                        vmtn_jobj.put("wheel",o.getWheel());
                        vmtn_jobj.put("service",o.getService());
                        vmtn_jobj.put("comment",o.getComment());
                        vmtn_jobj.put("user_id",user_id);
                        vmtn_jobj.put("created_time",o.getCreate_time());
                        vmtn_jarr.put(vmtn_jobj);
                    }
                }



                dvrdata_jObj=new JSONObject();
                dvrdata_jObj.put("vmtn_data",vmtn_jarr);
                dvrdata_jObj.put("vusage_data",vusage_jArr);

                String link=BASE_URL+"post_driver_data";
                String data=dvrdata_jObj.toString();
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
            Log.i("log tag", "Driver Data Post Result " + result);
            //progressDialog.dismiss();
         try {
            JSONObject resJobj=new JSONObject(result);
             if(resJobj.getBoolean("dvr_data_updated")) {
                 if (resJobj.getBoolean("vusage_updated")) {
                     atd_db.updated_vusage();
                     Toast.makeText(DriverHome_Activity.this, "Vehicle usage has been posted", Toast.LENGTH_SHORT).show();
                 }else{
                     Toast.makeText(DriverHome_Activity.this, "Vehicle usage can't posted. Try again!", Toast.LENGTH_SHORT).show();
                 }
                 if (resJobj.getBoolean("vmtn_updated")) {
                     atd_db.updated_vmtn();
                     Toast.makeText(DriverHome_Activity.this, "Vehicle maintenance has been posted", Toast.LENGTH_SHORT).show();
                 }else{
                     Toast.makeText(DriverHome_Activity.this, "Vehicle maintenance can't posted. Try again!", Toast.LENGTH_SHORT).show();
                 }
             }else{
                 Toast.makeText(DriverHome_Activity.this, "Can't update to server!", Toast.LENGTH_SHORT).show();
             }
         } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data" + e.toString());
        }

        }
    }

    private class getData extends AsyncTask<String,String,String> {

       private ProgressDialog progressDialog=ProgressDialog.show(DriverHome_Activity.this,"","Loading..");
        protected void onPreExecute(){}
        SimpleDateFormat date_time_fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date_fmt = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        protected String doInBackground(String... arg0){
            try {

                String link=BASE_URL+"getdriverdata";
                //String data= URLEncoder.encode("user_id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(user_id),"UTF-8");

                /// TEMP CODE //////////////
                JSONObject jobj= new JSONObject();
                jobj.put("user_id",user_id);
                Log.e("LOG TAG", "USER ID :" + user_id);
                Log.e("LOG TAG","USER DATE :" + retrive_date);

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
            } }

        @Override
        protected void onPostExecute(String result){

            Log.d("LOG TAG","GET DIV_SCHEDUE RESULT " +result);
            if(!result.equals("false")) {

                try {
                    jsonObj= new JSONObject(result);
                    schedujArr=jsonObj.getJSONArray("schedule");
                    vehiclejArr=jsonObj.getJSONArray("cars");
                    classjArr=jsonObj.getJSONArray("class");
                    lct_jarr=jsonObj.getJSONArray("vlocation");

                    // get schedule data and insert
                    if(schedujArr.length()>0) {
                        atd_db.clear_schedule();
                        atd_db.clear_location();
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

                            schedule_obj.setTeacher_id(jsonObj.getInt("teacherid"));
                            schedule_obj.setClass_id(jsonObj.getInt("class_id"));

                            if(jsonObj.getInt("teacherid")<1 )
                                schedule_obj.setTeacher(" ");
                            else
                                schedule_obj.setTeacher(jsonObj.getString("teachername"));

                            schedule_obj.setDriver(jsonObj.getString("drivername"));
                            schedule_obj.setDriver_id(jsonObj.getInt("driver_id"));
                            schedule_obj.setVehicle(jsonObj.getString("vehiclename"));
                            schedule_obj.setLocation(jsonObj.getString("location_desc"));
                            schedule_obj.setLocation_id(jsonObj.getInt("location_id"));

                            atd_db.insert_driver_schedule(schedule_obj);
                            Toast.makeText(DriverHome_Activity.this, "Inserted schedule !", Toast.LENGTH_SHORT).show();
                            //schedule_obj.setDate();
                        }
                    }else{
                        Toast.makeText(DriverHome_Activity.this, "No schedule !", Toast.LENGTH_SHORT).show();
                    }

                    if(classjArr.length()>0) {
                        atd_db.clear_class();
                        atd_db.clear_student();
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
                        Toast.makeText(DriverHome_Activity.this, "Inserted class!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(DriverHome_Activity.this, "No class !", Toast.LENGTH_SHORT).show();
                    }

                    if(vehiclejArr.length()>0){
                        //get vehicle data and insert
                        atd_db.clear_vehicle();
                        for (int i=0; i<vehiclejArr.length(); i++){
                            jsonObj = new JSONObject();
                            vehicle_obj= new VehicleObject();
                            jsonObj=vehiclejArr.getJSONObject(i);
                            vehicle_obj.setVehicle_id(jsonObj.getInt("vehicle_id"));
                            vehicle_obj.setVehicle_no(jsonObj.getString("vehiclename"));
                            vehicle_obj.setVehicle_model(jsonObj.getString("v_model"));
                            atd_db.insert_vehicle(vehicle_obj);

                        }
                        Toast.makeText(DriverHome_Activity.this, "Inserted vehicle !", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(DriverHome_Activity.this, "No vehicle!", Toast.LENGTH_SHORT).show();
                    }

                    if(lct_jarr.length()>0){
                        atd_db.clear_location();
                        for (int i=0; i<lct_jarr.length(); i++){
                            atd_db.insert_location(lct_jarr.getJSONObject(i));
                        }
                        Toast.makeText(DriverHome_Activity.this, "Inserted location !", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(DriverHome_Activity.this, "No location !", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.e("log_tag", "Error parsing data" + e.toString());
                }
                Log.i("LOG Tag","USER ID : " + user_id);

            }else{
                Toast.makeText(DriverHome_Activity.this, "There is no schedule to update !", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }

    public void closeDrawer(){
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    public void openDrawer(){ mDrawerLayout.openDrawer(mDrawerList); }
    // Add item to drawer
    private void addDrawerItems() {
        pref=getSharedPreferences("loginPrefs",PREFERENCE_MODE_PRIVATE);
        String token=pref.getString("token", null);
        urole_id = pref.getInt("role", 0);
        menuList=new ArrayList<String>();
        menuList.add("Vehicle Usage");
        menuList.add("Vehicle Maintenance");
        menuList.add("About");
        menuList.add("Setting");

        if(token!=null && urole_id==1){
            menuList.add("Teacher mode");
        }

        menuList.add("Logout");

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeDrawer();
                if(urole_id > 1 && position==4){
                    position=5;
                }

                if(position==0){
                    //Vehicle Usage
                    Intent intent = new Intent(getApplicationContext(), AddVehicleUsage_Activity.class);
                    startActivity(intent);
                }else if(position==1){
                    Intent intent = new Intent(getApplicationContext(), AddVehicleMaintenance_Activity.class);
                    startActivity(intent);
                }else if(position==5){
                    //Vehicle Usage
                    pref=getSharedPreferences("loginPrefs",PREFERENCE_MODE_PRIVATE);
                    editor=pref.edit();
                    editor.clear();
                    editor.commit();
                    finish();
                    Intent intent = new Intent(DriverHome_Activity.this, MainActivity.class);
                    startActivity(intent);
                }else if(position==4){
                    Intent intent = new Intent(DriverHome_Activity.this, HomeActivity.class);
                    startActivity(intent);
                }
                Log.i("Log_Tag","SIDE STRING ARR POSITION :"+position);
            }
        });
    }
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                //  Boolean res=;
                setDate();
//                if(MainActivity.isOnline()){
//                    if (atd_db.check_update_driver_data()) {
//                        // if(atd_db.check_update() || atd_db.check_stu_update()) {
//                        new post_driver_data().execute();
//                    } else {
//                        Toast.makeText(DriverHome_Activity.this, "There is no schedule to post !", Toast.LENGTH_SHORT).show();
//                    }
//                    new getData().execute();
//                }else{
//                    Toast.makeText(DriverHome_Activity.this, "No internet connection !", Toast.LENGTH_SHORT).show();
//                }
                return super.onOptionsItemSelected(item);
            case R.id.expanded_menu:
                openDrawer();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
