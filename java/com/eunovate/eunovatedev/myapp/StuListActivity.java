package com.eunovate.eunovatedev.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.eunovate.eunovatedev.myapp.dao_class.Stu_DBHelper;
import com.eunovate.eunovatedev.myapp.object.StudentObject;

import java.util.ArrayList;

public class StuListActivity extends AppCompatActivity {
    Stu_DBHelper mydb;
    FancyAdapter aa;
    private Toolbar myToolbar;
    ArrayList<StudentObject> arrayOfStuData=new ArrayList<StudentObject>();
    private int class_id,student_id;
    private Button atvBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_list);
        myToolbar = (Toolbar) findViewById(R.id.display_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_back_btn);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("class_id", class_id);
                dataBundle.putInt("from_stuList", 1);
                Intent intent = new Intent(getApplicationContext(), ClassDisplay.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });

        Bundle extras=getIntent().getExtras();

        if(extras!=null) {
            //Get class_id from intent
            class_id = extras.getInt("class_id");
            mydb = new Stu_DBHelper(this);
            showListView(class_id);
            //get Inactive Student from db

        }

    }

    private void showListView(int cid){
        arrayOfStuData=mydb.get_inactive_students(cid);
        ListView myListView=(ListView)findViewById(R.id.listView1);
        aa=new FancyAdapter();
        myListView.setAdapter(aa);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
                //TODO Auto-generated method stub
                int id_To_Search=arg2+1;

                Bundle dataBundle=new Bundle();
                dataBundle.putInt("id",id_To_Search);

                Intent intent=new Intent(getApplicationContext(),DisplayStudent.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    private void activate(int stuid){
        if(mydb.update_stu_activate(stuid, 0)){
            Toast.makeText(getApplicationContext(), "Activated !", Toast.LENGTH_SHORT).show();
            showListView(class_id);
            //finish();
            //Intent intent=new Intent(getApplicationContext(),StuListActivity.class);
            //startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"not Activated",Toast.LENGTH_SHORT).show();
        }
    }

    class FancyAdapter extends ArrayAdapter<StudentObject>{

        FancyAdapter(){
            super(StuListActivity.this, android.R.layout.activity_list_item, arrayOfStuData);
        }

        public View getView(int position, View convertView,ViewGroup parent){
            ViewHolder holder;

            if(convertView==null){
                LayoutInflater inflater=getLayoutInflater();
                convertView=inflater.inflate(R.layout.stu_row,null);
                holder = new ViewHolder(convertView);
                holder.activate=(Button) convertView.findViewById(R.id.activate);
                holder.activate.setText("Activate");
                holder.activate.setTag(new Integer(position));
                convertView.setTag(holder);
                holder.name.setTag(new Integer(position));
            }else
            {
                holder=(ViewHolder)convertView.getTag();
            }

            holder.name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int pos=(Integer)v.getTag();
                    StudentObject studentObject=arrayOfStuData.get(pos);
                    student_id=studentObject.getStudent_id();
                    Bundle dataBundle = new Bundle();
                    dataBundle.putInt("stu_id", student_id);
                    Intent intent = new Intent(getApplicationContext(), DisplayStudent.class);
                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            });

            holder.activate.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    //Button atvBtn=(Button) v;
                    int pos=(Integer)v.getTag();
                    StudentObject studentObject=arrayOfStuData.get(pos);
                    student_id=studentObject.getStudent_id();
                    new AlertDialog.Builder(StuListActivity.this)
                            .setTitle("Confirm !")
                            .setMessage("Do you want to activate this student?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    activate(student_id);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });

            holder.populateFrom(arrayOfStuData.get(position));
            return (convertView);
        }
    }

    class ViewHolder{

        public TextView name=null;
        public TextView locaiton=null;
        public ImageView imageView;
        public Button activate;

        ViewHolder(View row){
            name=(TextView)row.findViewById(R.id.name);
            locaiton=(TextView)row.findViewById(R.id.location);
        }

        void populateFrom(StudentObject stu){
            name.setText(stu.getName());
            locaiton.setText(stu.getLocation());
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

        switch (item.getItemId()){

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public  boolean onKeyDown(int keycode,KeyEvent event){
        if(keycode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode,event);
    }


}
