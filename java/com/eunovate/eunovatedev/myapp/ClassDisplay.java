package com.eunovate.eunovatedev.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.eunovate.eunovatedev.myapp.dao_class.Class_DBHelper;
import com.eunovate.eunovatedev.myapp.dao_class.DataBaseHelper;
import com.eunovate.eunovatedev.myapp.dao_class.Stu_DBHelper;
import com.eunovate.eunovatedev.myapp.object.StudentObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassDisplay extends AppCompatActivity {
    private Toolbar myToolbar;
    private DataBaseHelper dbHelper;
    private Class_DBHelper mydb;
    private TextView classname,location,vehicle,uname,cphone,caddress,city,township;
    private ListView myListView;
    private Button stuBtn,schBtn;
    private List<Map<String, String>> myArrList;
    private int value,param_curid;
    private int stu_id;
    private FancyAdapter aa;
    private View myview;
    private Intent intent;
    private Bundle dataBundle;
    private ArrayList<StudentObject> arrayOfStuData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_display);
        myToolbar = (Toolbar) findViewById(R.id.display_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_back_btn);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        classname=(TextView)findViewById(R.id.class_name);
        vehicle=(TextView)findViewById(R.id.vehicle);
        uname=(TextView)findViewById(R.id.user);
        cphone=(TextView) findViewById(R.id.cphone);
        caddress=(TextView) findViewById(R.id.caddress);
        township=(TextView) findViewById(R.id.township);
        city=(TextView) findViewById(R.id.city);

        schBtn=(Button)findViewById(R.id.schubtn);
        stuBtn=(Button)findViewById(R.id.stubtn);
        //Change button collor
        schBtn.setBackgroundColor(getResources().getColor(R.color.mygrey));

        dbHelper=new DataBaseHelper(this);
        mydb=new Class_DBHelper(this);

        Bundle extras=getIntent().getExtras();

        if(extras!=null) {
            //Get class_id from intent
            value=extras.getInt("class_id");
            param_curid=extras.getInt("course_id");

            // get class detail from db
            Cursor rs=mydb.getClassDtl(value);
            rs.moveToFirst();
            // get data from database rs object
            String clsname_val    = rs.getString(rs.getColumnIndex(dbHelper.CLASS_COLUMN_NAME));
            String location_val    = rs.getString(rs.getColumnIndex(dbHelper.CLASS_COLUMN_LOCATION));
            String vehicle_val    = rs.getString(rs.getColumnIndex(dbHelper.CLASS_COLUMN_VEHICLE));
            String cphone_val=rs.getString(rs.getColumnIndex(dbHelper.CLASS_COLUMN_PHONE));
            String caddress_val=rs.getString(rs.getColumnIndex(dbHelper.CLASS_COLUMN_ADDRESS));
            String city_val=rs.getString(rs.getColumnIndex(dbHelper.CLASS_COLUMN_CITY));

            if(!rs.isClosed()){rs.close();}
            // Set data to textview
            classname.setText(clsname_val.toString());
            classname.setFocusable(false);
            classname.setClickable(false);

            township.setText(location_val.toString());
            township.setFocusable(false);
            township.setClickable(false);

            city.setText(city_val.toString());
            city.setFocusable(false);
            city.setClickable(false);

            vehicle.setText(vehicle_val.toString());
            vehicle.setFocusable(false);
            vehicle.setClickable(false);

            cphone.setText(cphone_val.toString());
            cphone.setFocusable(false);
            cphone.setClickable(false);
            caddress.setText(caddress_val.toString());
            caddress.setFocusable(false);
            caddress.setClickable(false);
            vehicle.setText(vehicle_val.toString());
            vehicle.setFocusable(false);
            vehicle.setClickable(false);



            int check_from_stulist=extras.getInt("from_stuList");
            if(check_from_stulist>0){
                getStu(findViewById(android.R.id.content));
            }else{
                getSchu(findViewById(android.R.id.content));
            }
        }
    }
    private void activate(int stuid,int course_id,int active_chk){
    //private void deactivate(int stuid){

        Stu_DBHelper stu_db=new Stu_DBHelper(this);
        Log.i("LOG TAG","ACTIVATE CHECK"+active_chk);
        //Check student deactivate
        if(active_chk==0) {
            if (stu_db.update_stu_activate(stuid, 1)) {
                Toast.makeText(getApplicationContext(), "Activated !", Toast.LENGTH_SHORT).show();
                getStu(myview);
            } else {
                Toast.makeText(getApplicationContext(), "not Activated", Toast.LENGTH_SHORT).show();
            }
        }else{
            Log.i("LOG TAG","UPDATE PARAM student_id:" + stuid + " course_id: " + course_id + " class_id : " + value);
            if (stu_db.update_sub_stu_activate(stuid, course_id, value)) {
                Toast.makeText(getApplicationContext(), "Activated !", Toast.LENGTH_SHORT).show();
                getStu(myview);
            } else {
                Toast.makeText(getApplicationContext(), "not Activated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //get student method
    public void getStu(View v){
        //change button color
        schBtn.setBackgroundColor(getResources().getColor(R.color.lightblue));
        stuBtn.setBackgroundColor(getResources().getColor(R.color.mygrey));
        arrayOfStuData=mydb.getStuList(value,param_curid);
        Log.i("LOG TAG", "CLASSID " + value);
        Log.i("LOG TAG", "COURSE ID  " + param_curid);

        myListView = (ListView) findViewById(R.id.my_list_view);
        aa=new FancyAdapter();
        myListView.setAdapter(aa);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                String strTxt = myArrList.get(position).toString();
                int stu_id = Integer.parseInt(myArrList.get(position).get("stu_id"));
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("stu_id", stu_id);
                Intent intent = new Intent(getApplicationContext(), DisplayStudent.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    //Get Schedule data method

    public void getSchu(View v){
        //change button color
        schBtn.setBackgroundColor(getResources().getColor(R.color.mygrey));
        stuBtn.setBackgroundColor(getResources().getColor(R.color.lightblue));
        //get schedule list from db
        myArrList=mydb.getSchList(value);
        //get adapter to set schedule list to list view
        SimpleAdapter adapter = new SimpleAdapter(this, myArrList,android.R.layout.simple_list_item_2,new String[] {"course", "lesson"},
                new int[] {android.R.id.text1,android.R.id.text2});
        myListView = (ListView) findViewById(R.id.my_list_view);
        //set adater to list view
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                onBackPressed();
//                String strTxt = myArrList.get(position).toString();
//                int stu_id = Integer.parseInt(myArrList.get(position).get("stu_id"));
//                Bundle dataBundle = new Bundle();
//                dataBundle.putInt("stu_id", stu_id);
//                Intent intent = new Intent(getApplicationContext(), DisplayStudent.class);
//                intent.putExtras(dataBundle);
//                startActivity(intent);
            }
        });
    }

    class FancyAdapter extends ArrayAdapter<StudentObject> {

        FancyAdapter(){
            super(ClassDisplay.this, android.R.layout.activity_list_item, arrayOfStuData);
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
                   stu_id=studentObject.getStudent_id();
                   Bundle dataBundle = new Bundle();
                   dataBundle.putInt("stu_id", stu_id);

                   // Can't go to student detail page
//                   Intent intent = new Intent(getApplicationContext(), DisplayStudent.class);
//                   intent.putExtras(dataBundle);
//                   startActivity(intent);
               }
           });

            holder.activate.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    //Button atvBtn=(Button) v;
                    int pos=(Integer)v.getTag();
                    StudentObject studentObject=arrayOfStuData.get(pos);
                    stu_id=studentObject.getStudent_id();
                    final int course_id = studentObject.getCourse_id();
                    final int act_chk = studentObject.getIs_active();
                    new AlertDialog.Builder(ClassDisplay.this)
                            .setTitle("Confirm !")
                            .setMessage("Do you want to deactivate this student?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    activate(stu_id,course_id,act_chk);
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
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.addStudent:
                dataBundle = new Bundle();
                dataBundle.putInt("stu_id", 0);
                dataBundle.putInt("class_id", value);
                intent = new Intent(getApplicationContext(), DisplayStudent.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
//            case R.id.showInacti:
//                dataBundle=new Bundle();
//                dataBundle.putInt("class_id", value);
//                intent = new Intent(getApplicationContext(), StuListActivity.class);
//                intent.putExtras(dataBundle);
//                startActivity(intent);
//                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);

        }

    }

}
