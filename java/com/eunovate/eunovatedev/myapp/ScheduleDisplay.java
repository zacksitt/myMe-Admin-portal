package com.eunovate.eunovatedev.myapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eunovate.eunovatedev.myapp.dao_class.AttendanceDBHelper;
import com.eunovate.eunovatedev.myapp.dao_class.DataBaseHelper;
import com.eunovate.eunovatedev.myapp.dao_class.Schedule_DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class ScheduleDisplay extends AppCompatActivity {
    private Toolbar myToolbar;
    private TextView clsname,curname,date,stime,etime,teacher,lesson,vehicle,driver,address,phone,user,classid,level;
    private Schedule_DBHelper mydb;
    private DataBaseHelper dbHelper;
    private int paramVal,role_id,course_id;
    private Timer timer;
    private Button atdBtn;
    private SharedPreferences pref;
    private Date dateval,etimeval,stimeval;
    private  SharedPreferences.Editor editor;
    private String clsname_val,lesson_val,courname_val,classid_val,level_val,address_val,phone_val;
    private SimpleDateFormat date_fmt,time_fmt,datetime_fmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_display);
        myToolbar = (Toolbar) findViewById(R.id.display_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_back_btn);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        pref=getSharedPreferences("loginPrefs", 0);
        role_id=pref.getInt("role", 0);


        atdBtn=(Button) findViewById(R.id.atd_btn);


        date=(TextView)findViewById(R.id.schedule_date);
        stime=(TextView)findViewById(R.id.stime);
        etime=(TextView)findViewById(R.id.etime);
        teacher=(TextView)findViewById(R.id.teacher);
        lesson=(TextView)findViewById(R.id.lesson);
        vehicle=(TextView)findViewById(R.id.vehicle);
        driver=(TextView)findViewById(R.id.driver);
        address=(TextView)findViewById(R.id.caddress);
        phone=(TextView)findViewById(R.id.cphone);
        user=(TextView)findViewById(R.id.user);
        classid=(TextView)findViewById(R.id.classid);
        curname=(TextView)findViewById(R.id.course);
        clsname=(TextView)findViewById(R.id.class_name);
        level=(TextView) findViewById(R.id.level);

        dbHelper=new DataBaseHelper(this);
        mydb=new Schedule_DBHelper(this);

        Bundle extras=getIntent().getExtras();

        if(extras!=null){
            paramVal=extras.getInt("param");
            Log.i("LOG TAG", "PARAM : " + paramVal);
            Cursor rs=mydb.getScheduleDtl(paramVal);

            rs.moveToFirst();

            time_fmt = new SimpleDateFormat("h:mm a");
            date_fmt = new SimpleDateFormat("yyyy-MM-dd");
            datetime_fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            String date_val= rs.getString(rs.getColumnIndex(dbHelper.SCHEDULE_COLUMN_DATE));
            String stime_val = rs.getString(rs.getColumnIndex(dbHelper.SCHEDULE_COLUMN_STIME));
            String etime_val = rs.getString(rs.getColumnIndex(dbHelper.SCHEDULE_COLUMN_ETIME));
            course_id=rs.getInt(rs.getColumnIndex(dbHelper.CUR_COLUMN_ID));


            try {
                dateval   = date_fmt.parse(date_val);
                stimeval = datetime_fmt.parse(stime_val);
                etimeval= datetime_fmt.parse(etime_val);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String teacher_val = rs.getString(rs.getColumnIndex(dbHelper.SCHEDULE_COLUMN_TEACHER));
            String vehicle_val = rs.getString(rs.getColumnIndex(dbHelper.SCHEDULE_COLUMN_VEHICLE));
            String driver_val = rs.getString(rs.getColumnIndex(dbHelper.SCHEDULE_COLUMN_DRIVER));
            clsname_val    = rs.getString(rs.getColumnIndex(dbHelper.CLASS_COLUMN_NAME));

            if(role_id==4)
            {
                lesson_val = rs.getString(rs.getColumnIndex(dbHelper.SCHEDULE_COLUMN_LESSON));
                classid_val = rs.getString(rs.getColumnIndex(dbHelper.CLASS_COLUMN_ID));
                level_val=rs.getString(rs.getColumnIndex(dbHelper.SCHECULE_COLUMN_LEVEL));
                courname_val= rs.getString(rs.getColumnIndex(dbHelper.SCHEDULE_COLUMN_COURSE));
            }else{
                address_val=rs.getString(rs.getColumnIndex(dbHelper.CLASS_COLUMN_ADDRESS));
                phone_val=rs.getString(rs.getColumnIndex(dbHelper.CLASS_COLUMN_PHONE));
            }

            if(rs.getInt(rs.getColumnIndex(dbHelper.SCHEDULE_COLUMN_RECORDED)) > 0){
           // if(mydb.check_schedule(paramVal)){
                atdBtn.setVisibility(View.INVISIBLE);
            }
            if(!rs.isClosed()){rs.close();}

            if(role_id==4){


                TextView add_lbl=(TextView) findViewById(R.id.caddresLBL);
                TextView phone_lbl=(TextView) findViewById(R.id.cphoneLBL);
                add_lbl.setVisibility(View.GONE);
                phone_lbl.setVisibility(View.GONE);

                address.setVisibility(View.GONE);
                phone.setVisibility(View.GONE);

                curname.setText(courname_val.toString());
                curname.setFocusable(false);
                curname.setClickable(true);

                lesson.setText(lesson_val.toString());
                lesson.setFocusable(false);
                lesson.setClickable(false);

                classid.setText(classid_val.toString());
                classid.setFocusable(false);
                classid.setClickable(false);

                level.setText(level_val.toString());
                level.setFocusable(false);
                level.setClickable(false);

            }else{
                address.setText(address_val.toString());
                address.setFocusable(false);
                address.setClickable(true);

                phone.setText(phone_val.toString());
                phone.setFocusable(false);
                phone.setClickable(false);

                clsname.setClickable(false);

                TextView cour_lbl=(TextView) findViewById(R.id.courseLBL);
                TextView lesson_lbl=(TextView) findViewById(R.id.lessonLBL);
                TextView level_lbl=(TextView) findViewById(R.id.levelLBL);
                atdBtn.setVisibility(View.INVISIBLE);
                cour_lbl.setVisibility(View.GONE);
                lesson_lbl.setVisibility(View.GONE);
                curname.setVisibility(View.GONE);
                lesson.setVisibility(View.GONE);
                level.setVisibility(View.GONE);
                level_lbl.setVisibility(View.GONE);
            }



            SimpleDateFormat mydate_fmt= new SimpleDateFormat("dd/MM/yyyy");
            clsname.setText(clsname_val.toString());
            clsname.setFocusable(false);
            clsname.setClickable(true);

            date.setText(mydate_fmt.format(dateval).toString());
            date.setFocusable(false);
            date.setClickable(false);

            stime.setText(time_fmt.format(stimeval).toString());
            stime.setFocusable(false);
            stime.setClickable(false);

            etime.setText(time_fmt.format(etimeval).toString());
            etime.setFocusable(false);
            etime.setClickable(false);

            teacher.setText(teacher_val.toString());
            teacher.setFocusable(false);
            teacher.setClickable(false);


            vehicle.setText(vehicle_val.toString());
            vehicle.setFocusable(false);
            vehicle.setClickable(false);

            driver.setText(driver_val.toString());
            driver.setFocusable(false);
            driver.setClickable(false);


        }
    }

    public void classDtl(View view){
        if(role_id==4) {
            String selected = ((TextView) findViewById(R.id.classid)).getText().toString();
            int id_To_Search = Integer.parseInt(selected);
            Log.i("LOG TAG : ", "SELECTED CLASS ID" + id_To_Search);

            Bundle dataBundle = new Bundle();
            dataBundle.putInt("class_id", id_To_Search);
            dataBundle.putInt("course_id",course_id);
            dataBundle.putInt("from_stuList", 0);
            Intent intent = new Intent(getApplicationContext(), ClassDisplay.class);
            intent.putExtras(dataBundle);
            startActivity(intent);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            myToolbar.setNavigationIcon(R.drawable.ic_back);
            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public void attendance(View v){

        String selected=((TextView)findViewById(R.id.classid)).getText().toString();
        int id_To_Search=Integer.parseInt(selected);
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("class_id", id_To_Search);
        dataBundle.putInt("schedule_id", paramVal);
        dataBundle.putInt("course_id",course_id);

        //Intent intent = new Intent(getApplicationContext(), AttendanceActivity.class);
        Intent intent = new Intent(getApplicationContext(), AttendanceRecord_Activity.class);
        intent.putExtras(dataBundle);
        startActivity(intent);

    }


    public  boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
