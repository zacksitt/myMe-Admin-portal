package com.eunovate.eunovatedev.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.eunovate.eunovatedev.myapp.dao_class.AttendanceInfoDBHelper;

public class AttendanceInfo extends AppCompatActivity {

    private TextView stu_count_textview,bvr_avg_textview;
    private Toolbar myToolbar;
    private AttendanceInfoDBHelper atd_info_db;
    private int param_schedule_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_info);
        myToolbar = (Toolbar) findViewById(R.id.display_toolbar);
        setSupportActionBar(myToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
//        myToolbar.setNavigationIcon(R.drawable.ic_back_btn);
//        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //What to do on back clicked
//                //handleOnBackPress();
//                onBackPressed();
//            }
//        });

        Bundle extras=getIntent().getExtras();
        param_schedule_id=extras.getInt("schedule_id");

        stu_count_textview=(TextView) findViewById(R.id.prestu);
        bvr_avg_textview=(TextView) findViewById(R.id.bvr_avg);

        atd_info_db=new AttendanceInfoDBHelper(this);
        int stu_count = atd_info_db.getPresentStudent(param_schedule_id);
        stu_count_textview.setText(Integer.toString(stu_count));
        stu_count_textview.setFocusable(false);
        stu_count_textview.setClickable(false);
    }

    //When user tab on finish button
    public void finish(View view){
        Boolean res = atd_info_db.set_record(param_schedule_id);
        if(res){
            Intent actChange = new Intent(AttendanceInfo.this, HomeActivity.class);
            startActivity(actChange);
        }
    }

    public  boolean onCreateOptionsMenu(Menu menu){

        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        return true;

    }
}
