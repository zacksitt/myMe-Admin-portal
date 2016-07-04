package com.eunovate.eunovatedev.myapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eunovate.eunovatedev.myapp.dao_class.DataBaseHelper;
import com.eunovate.eunovatedev.myapp.dao_class.Stu_DBHelper;
import com.eunovate.eunovatedev.myapp.object.StudentObject;

import java.util.ArrayList;

public class DisplayStudent extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {
    private Stu_DBHelper mydb;
    private Toolbar myToolbar;
    private DataBaseHelper dbHelper;
    private Spinner spnrGender;
    TextView name,address,location,contact,date_of_birth,father_name,father_nrc_no,mother_name,mother_nrc_no,remark,nrc_no,gender;
    int id_To_Update,class_id=0,schedule_id,gender_chk = 1,course_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new DataBaseHelper(DisplayStudent.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_student);
        myToolbar = (Toolbar) findViewById(R.id.display_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_back_btn);
        //catch event when user click back button from toolbar
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // get text view from xml

        name=(TextView) findViewById(R.id.editTextName);
        address=(TextView) findViewById(R.id.editTextAddress);
        //location=(TextView) findViewById(R.id.editTextLocation);
        //contact=(TextView) findViewById(R.id.editTextContact);
        date_of_birth=(TextView) findViewById(R.id.editTextDateOfBirth);
        father_name=(TextView) findViewById(R.id.editTextFatherName);
       // father_nrc_no=(TextView) findViewById(R.id.editTextFatherNrcNo);
        mother_name=(TextView) findViewById(R.id.editTextMotherName);
       // mother_nrc_no=(TextView) findViewById(R.id.editTextMotherNrcNo);
        //nrc_no=(TextView) findViewById(R.id.editTextRemark);
        //remark=(TextView) findViewById(R.id.editTextNRC);
       // gender=(TextView) findViewById(R.id.editTextGender);
        spnrGender=(Spinner) findViewById(R.id.spnrGender);

        ArrayList<String> genders=new ArrayList<>();
        genders.add("Male");
        genders.add("Female");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genders);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnrGender.setAdapter(dataAdapter);
        spnrGender.setOnItemSelectedListener(this);

        mydb=new Stu_DBHelper(this);

//        // create studnet db helper object
//
//
//        Bundle extras=getIntent().getExtras();
//        if(extras!=null){
//            //get stu_id that set on previous page
//            int Value=extras.getInt("stu_id");
//            class_id=extras.getInt("class_id");
//            schedule_id=extras.getInt("schedule_id");
//
//            if(Value>0){
//                //get student detail from db
//                Cursor rs=mydb.getData(Value);
//                id_To_Update=Value;
//                rs.moveToFirst();
//                // get value from cursor database object
//                String name_    = rs.getString(rs.getColumnIndex(dbHelper.STU_COLUMN_NAME));
//                String address_ = rs.getString(rs.getColumnIndex(dbHelper.STU_COLUMN_ADDRESS));
//                String location_= rs.getString(rs.getColumnIndex(dbHelper.STU_COLUMN_LOCATION));
//                String contact_ = rs.getString(rs.getColumnIndex(dbHelper.STU_COLUMN_CONTACT));
//                String date_of_birth_ = rs.getString(rs.getColumnIndex(dbHelper.STU_COLUMN_DATEOFBIRTH));
//                String father_name_ = rs.getString(rs.getColumnIndex(dbHelper.STU_COLUMN_FATHERNAME));
//                String father_nrc_ = rs.getString(rs.getColumnIndex(dbHelper.STU_COLUMN_FATHERNRCNO));
//                String mother_name__ = rs.getString(rs.getColumnIndex(dbHelper.STU_COLUMN_MOTHERNAME));
//                String mother_nrc__ = rs.getString(rs.getColumnIndex(dbHelper.STU_COLUMN_MOTHERNRCNO));
//                String remark__ = rs.getString(rs.getColumnIndex(dbHelper.STU_COLUMN_NRCNO));
//                String nrc_no__ = rs.getString(rs.getColumnIndex(dbHelper.STU_COLUMN_REMARK));
//                String gender__ = rs.getString(rs.getColumnIndex(dbHelper.STU_COLUMN_GENDER));
//
//                if(!rs.isClosed()){rs.close();}
//
//                Button b=(Button)findViewById(R.id.button1);
//                b.setVisibility(View.INVISIBLE);
//                // set value that select from database set to list view.
//                name.setText(name_.toString());
//                name.setFocusable(false);
//                name.setClickable(false);
//
//                address.setText(address_.toString());
//                address.setFocusable(false);
//                address.setClickable(false);
//
//                location.setText(location_.toString());
//                location.setFocusable(false);
//                location.setClickable(false);
//
//                contact.setText(contact_.toString());
//                contact.setFocusable(false);
//                contact.setClickable(false);
//
//                date_of_birth.setText(date_of_birth_.toString());
//                date_of_birth.setFocusable(false);
//                date_of_birth.setClickable(false);
//
//                father_name.setText(father_name_.toString());
//                father_name.setFocusable(false);
//                father_name.setClickable(false);
//
//                father_nrc_no.setText(father_nrc_.toString());
//                father_nrc_no.setFocusable(false);
//                father_nrc_no.setClickable(false);
//
//                mother_name.setText(mother_name__.toString());
//                mother_name.setFocusable(false);
//                mother_name.setClickable(false);
//
//                mother_nrc_no.setText(mother_nrc__.toString());
//                mother_nrc_no.setFocusable(false);
//                mother_nrc_no.setClickable(false);
//
//                remark.setText(remark__.toString());
//                remark.setFocusable(false);
//                remark.setClickable(false);
//
//                nrc_no.setText(nrc_no__.toString());
//                nrc_no.setFocusable(false);
//                nrc_no.setClickable(false);
//
//                gender.setText(gender__.toString());
//                gender.setFocusable(false);
//                gender.setClickable(false);
//            }
//        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        if(item.equals("Male"))
            gender_chk=1;
        else
            gender_chk=2;
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void run(View view){

        String test=name.getText().toString();
        if(name.getText().toString().equals("") || address.getText().toString().equals("") ){

            Toast.makeText(getApplicationContext(),"Name,Address and Gender are requried !",Toast.LENGTH_SHORT).show();

        }else{

         Bundle extras=getIntent().getExtras();
        if(extras!=null){
            course_id=extras.getInt("course_id");
            class_id=extras.getInt("class_id");
            schedule_id=extras.getInt("schedule_id");
            StudentObject stuObj=new StudentObject();
            stuObj.setStudent_id(id_To_Update);
            stuObj.setClass_id(class_id);
            stuObj.setName(name.getText().toString());
            stuObj.setAddress(address.getText().toString());
           // stuObj.setLocation(location.getText().toString());
            stuObj.setDate_of_birth(date_of_birth.getText().toString());
            stuObj.setFather_name(father_name.getText().toString());
            //stuObj.setFather_nrc_no(father_nrc_no.getText().toString());
            stuObj.setMother_name(mother_name.getText().toString());
            //stuObj.setMother_nrc_no(mother_nrc_no.getText().toString());
            //stuObj.setRemark(remark.getText().toString());
            //stuObj.setNrc_no(nrc_no.getText().toString());
            //stuObj.setContact(contact.getText().toString());
            stuObj.setGender(gender_chk);

                if(mydb.insertStuToClass(stuObj,course_id)){

                    Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Not done", Toast.LENGTH_SHORT).show();
                }
                //finish();
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("class_id", class_id);
                dataBundle.putInt("course_id",course_id);
                dataBundle.putInt("schedule_id",schedule_id);
                //Intent intent=new Intent(getApplicationContext(),ClassDisplay.class);
                Intent intent=new Intent(getApplicationContext(),AttendanceRecord_Activity.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
//
//            if(Value>0){
//                if(mydb.updateContact(stuObj)){
//                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(getApplicationContext(),StuListActivity.class);
//                    startActivity(intent);
//                }else{
//                    Toast.makeText(getApplicationContext(),"not Updated",Toast.LENGTH_SHORT).show();
//                }
//            }else{
//                if(mydb.insertStuToClass(stuObj)){
//
//                    Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getApplicationContext(), "Not done", Toast.LENGTH_SHORT).show();
//                }
//                //finish();
//                Bundle dataBundle = new Bundle();
//                dataBundle.putInt("class_id", class_id);
//                dataBundle.putInt("schedule_id",schedule_id);
//                //Intent intent=new Intent(getApplicationContext(),ClassDisplay.class);
//                Intent intent=new Intent(getApplicationContext(),AttendanceRecord_Activity.class);
//                intent.putExtras(dataBundle);
//                startActivity(intent);
//            }
        }
    }

    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        Bundle extras=getIntent().getExtras();
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return  true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        super.onOptionsItemSelected(menuItem);
        switch (menuItem.getItemId()){

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
