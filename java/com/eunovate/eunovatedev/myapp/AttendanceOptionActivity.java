package com.eunovate.eunovatedev.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class AttendanceOptionActivity extends AppCompatActivity {
    private int paramVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_option);
        Toolbar toolbar = (Toolbar) findViewById(R.id.display_toolbar);
        setSupportActionBar(toolbar);
    }

    public void present(View v){
        Bundle extras=getIntent().getExtras();
        paramVal=extras.getInt("class_id");
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("class_id", paramVal);
        Intent intent = new Intent(getApplicationContext(), AttendanceActivity.class);
        intent.putExtras(dataBundle);
        startActivity(intent);
    }

    public void absent(View v){
        Bundle extras=getIntent().getExtras();
        paramVal=extras.getInt("class_id");
        Log.i("LOGTAG ","PARAM ID"+paramVal);
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("class_id", paramVal);
        Intent intent = new Intent(getApplicationContext(), AttendanceActivity.class);
        intent.putExtras(dataBundle);
        startActivity(intent);

    }
}
