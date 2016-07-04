package com.eunovate.eunovatedev.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.eunovate.eunovatedev.myapp.dao_class.AttendanceDBHelper;
import com.eunovate.eunovatedev.myapp.dao_class.AttendanceInfoDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Noti_Activity extends AppCompatActivity {
    private AttendanceDBHelper atd_db;
    private ListView myListView;
    private List<Map<String, String>> myArrList;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_);
        myToolbar = (Toolbar) findViewById(R.id.display_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_back_btn);
        //catch event when user click back button from toolbar
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // onBackPressed();
                Intent intent = new Intent(Noti_Activity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        myListView=(ListView) findViewById(R.id.notiList);
        set_noti_view();
    }

    private void set_noti_view(){
        atd_db=new AttendanceDBHelper(this);
        myArrList=atd_db.get_noti_list();
        Log.i("LogTag", "My ARR List " + myArrList);

        SimpleAdapter adapter = new SimpleAdapter(this, myArrList,android.R.layout.simple_list_item_2,new String[] {"description", "date"},
                new int[] {android.R.id.text1,android.R.id.text2});

        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                //TODO Auto-generated method stub

            }
        });

        atd_db.seen_noti();
    }
}
